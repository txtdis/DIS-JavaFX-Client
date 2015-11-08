package ph.txtdis.service;

import static java.lang.Integer.valueOf;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.stream.Collectors.toList;
import static ph.txtdis.util.NumberUtils.divide;
import static ph.txtdis.util.NumberUtils.formatCurrency;
import static ph.txtdis.util.NumberUtils.isZero;
import static ph.txtdis.util.NumberUtils.toPercentRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import lombok.NoArgsConstructor;
import ph.txtdis.dto.AbstractSoldOrder;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Discount;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.dto.Tracked;
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.DifferentDiscountException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.NotAnItemToBeSoldToCustomerException;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;
import ph.txtdis.util.NumberUtils;
import ph.txtdis.util.Util;

@NoArgsConstructor
public abstract class SoldService<T extends AbstractSoldOrder<PK>, PK>
		implements Audited, Reset, Serviced<T, PK>, SpunById<PK>, Tracked
{

	@Autowired
	private ItemFamilyService familyService;

	@Autowired
	protected ItemService itemService;

	@Autowired
	private ReadOnlyService<T> readOnlyService;

	@Autowired
	private SavingService<T> savingService;

	@Autowired
	private SpunService<T, PK> spunService;

	@Autowired
	private VatService vatService;

	private BigDecimal unitPrice, vatDivisor;

	private Customer customer;

	private Item item;

	private List<UomType> sellingUoms;

	private List<Discount> discounts;

	private List<VolumeDiscount> volumeDiscounts;

	private T entity;

	SoldService(ItemFamilyService familyService, ItemService itemService, ReadOnlyService<T> readOnlyService,
			SavingService<T> savingService, SpunService<T, PK> spunService) {
		this.familyService = familyService;
		this.itemService = itemService;
		this.readOnlyService = readOnlyService;
		this.savingService = savingService;
		this.spunService = spunService;
	}

	public void checkforDuplicates(String id) throws Exception {
		if (readOnlyService.module(getModule()).getOne("/" + id) != null)
			throw new DuplicateException(getModuleId() + id);
	}

	public BillableDetail createDetail(UomType uom, BigDecimal qty, QualityType quality) {
		BillableDetail sd = new BillableDetail();
		sd.setId(item.getId());
		sd.setItemName(item.getName());
		sd.setUom(uom);
		sd.setQty(qty);
		sd.setQuality(quality);
		sd.setPriceValue(computeUnitPrice(uom, qty));
		return sd;
	}

	@Override
	public T get() {
		if (entity == null)
			reset();
		return entity;
	}

	@Override
	public String getAuditedBy() {
		return get().getAuditedBy();
	}

	@Override
	public ZonedDateTime getAuditedOn() {
		return get().getAuditedOn();
	}

	@Override
	public String getCreatedBy() {
		return get().getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return get().getCreatedOn();
	}

	public CreditDetail getCredit() {
		try {
			return customer.getCreditDetails().stream().filter(p -> p.getStartDate().compareTo(getOrderDate()) <= 0)
					.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get();
		} catch (Exception e) {
			return null;
		}
	}

	public List<BillableDetail> getDetails() {
		if (get().getDetails() == null)
			setDetails(Collections.emptyList());
		return get().getDetails();
	}

	public BigDecimal getDiscountValue() {
		BigDecimal discountValue = ZERO;
		if (discounts != null) {
			BigDecimal grossValue = getGross();
			for (Discount d : discounts) {
				discountValue = discountValue.add(grossValue.multiply(NumberUtils.toPercentRate(d.getPercent())));
				grossValue = grossValue.subtract(discountValue);
			}
		}
		return discountValue;
	}

	@Override
	public PK getId() {
		return get().getId();
	}

	@Override
	public Boolean getIsValid() {
		return get().getIsValid();
	}

	public String getItemDescription() {
		return item == null ? null : item.getDescription();
	}

	@Override
	public ReadOnlyService<T> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public SavingService<T> getSavingService() {
		return savingService;
	}

	public List<UomType> getSellingUoms() throws Exception {
		if (sellingUoms == null)
			setSellingUoms(itemService.listSellingUoms(item));
		return sellingUoms;
	}

	@Override
	public SpunService<? extends Keyed<PK>, PK> getSpunService() {
		return spunService;
	}

	public BigDecimal getTotal() {
		return get().getTotalValue();
	}

	public BigDecimal getVat() throws Exception {
		return getTotal() == null ? null : getTotal().subtract(getVatable());
	}

	public BigDecimal getVatable() throws Exception {
		return getTotal() == null ? null : divide(getTotal(), getVatDivisor());
	}

	@Override
	public void reset() {
		discounts = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void set(Keyed<PK> entity) {
		if (entity != null)
			this.entity = (T) entity;
	}

	public void setDetails(List<BillableDetail> details) {
		if (isNew()) {
			get().setDetails(details);
			updateTotals();
		}
	}

	@Override
	public void setIsValid(Boolean isValid) {
		get().setIsValid(isValid);
	}

	public void setItemUponValidation(Long id) throws Exception {
		item = null;
		item = validateItem(id);
	}

	public void setOrderDateUponValidation(LocalDate date) throws Exception {
		if (date == null)
			return;
		if (date.isAfter(LocalDate.now()))
			throw new DateInTheFutureException();
		setOrderDateAfterReset(date);
	}

	private boolean areChannelLimitsEqual(VolumeDiscount vd) {
		return Util.areEqual(vd.getChannelLimit(), customer.getChannel());
	}

	private Comparator<VolumeDiscount> compareSetTypeVolumeDiscountChannelLimits() {
		return (a, b) -> compareVolumeDiscountChannelLimits(a, b);
	}

	private Comparator<VolumeDiscount> compareTierTypeVolumeDiscountChannelLimitsThenReverseCutOff() {
		return (a, b) -> reverseCompareCutOffsWhenChannelLimitsAreEqual(a, b);
	}

	private int compareVolumeDiscountChannelLimits(VolumeDiscount a, VolumeDiscount b) {
		Channel c1 = a.getChannelLimit();
		Channel c2 = b.getChannelLimit();
		if (c1 == null)
			return c2 == null ? 0 : 1;
		return c2 == null ? -1 : c1.compareTo(c2);
	}

	private BigDecimal computeGross() {
		try {
			return getDetails().stream().map(d -> d.getQty().multiply(d.getPriceValue())).reduce(ZERO,
					(a, b) -> a.add(b));
		} catch (Exception e) {
			return null;
		}
	}

	private BigDecimal computeTotal() {
		return getGross().subtract(getDiscountValue());
	}

	private BigDecimal computeUnitPrice(UomType uom, BigDecimal qty) {
		BigDecimal qtyPerUom = getQtyPerUom(uom);
		BigDecimal discountedPrice = computeDiscountedPrice(qty.multiply(qtyPerUom));
		return discountedPrice.multiply(qtyPerUom);
	}

	private void confirmItemDiscountEqualsCurrent(Item i) throws Exception {
		if (customerNeverHadDiscountsOrItemIsFirstTableEntry(i))
			return;
		if (!discounts.equals(getLatestDiscounts(i)))
			throw new DifferentDiscountException();
	}

	private Item confirmItemExists(Long id) throws Exception {
		return itemService.find(id);
	}

	private void confirmItemIsAllowedToBeSoldToCurrentCustomer(Item item) throws Exception {
		try {
			setLatestPrice(item);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotAnItemToBeSoldToCustomerException(item, customer);
		}
	}

	private void confirmItemIsNotOnList(Item i) throws Exception {
		if (getDetails().stream().filter(d -> d.getId() == i.getId()).findAny().isPresent())
			throw new DuplicateException(i.getName());
	}

	private String createEachLevelDiscountText(Discount d, BigDecimal total, BigDecimal net) {
		BigDecimal perLevel = net.multiply(toPercentRate(d.getPercent()));
		total = total.add(perLevel);
		net = net.subtract(total);
		return "[" + d.getLevel() + ": " + d.getPercent() + "%] " + formatCurrency(perLevel);
	}

	private boolean customerNeverHadDiscountsOrItemIsFirstTableEntry(Item i) throws Exception {
		if (discounts != null && !getDetails().isEmpty())
			return discounts.isEmpty();
		discounts = getLatestDiscounts(i);
		get().setDiscountIds(listDiscountIds());
		return true;
	}

	private BigDecimal getCutOff(VolumeDiscount vd) {
		BigDecimal cutoff = new BigDecimal(vd.getCutoff());
		BigDecimal qtyPerUom = getQtyPerUom(vd.getUom());
		return cutoff.multiply(qtyPerUom);
	}

	private List<String> getEachLevelDiscountTextList(List<String> list) {
		BigDecimal net = getGross();
		discounts.forEach(d -> list.add(createEachLevelDiscountText(d, ZERO, net)));
		return list;
	}

	private List<ItemFamily> getFamilies(Item item) throws Exception {
		return familyService.getItemAncestry(item);
	}

	private BigDecimal getGross() {
		return get().getGrossValue();
	}

	private ItemFamily getHighestTierFamilyAmongDiscountLimits(List<ItemFamily> families) {
		try {
			return getLatestCustomerDiscountStream().filter(cd -> families.contains(cd.getFamilyLimit()))
					.map(cd -> cd.getFamilyLimit()).max((a, b) -> ordinal(a).compareTo(ordinal(b))).get();
		} catch (Exception e) {
			return null;
		}
	}

	private Stream<Discount> getLatestCustomerDiscountStream() {
		try {
			return customer.getDiscounts().stream().filter(cd -> cd.getStartDate().compareTo(getOrderDate()) <= 0);
		} catch (Exception e) {
			return Stream.empty();
		}
	}

	private List<Discount> getLatestDiscounts(Item i) throws Exception {
		List<ItemFamily> l = getFamilies(i);
		ItemFamily f = getHighestTierFamilyAmongDiscountLimits(l);
		return getLatestDiscounts(f);
	}

	private List<Discount> getLatestDiscounts(ItemFamily f) {
		return getLatestFamilyFilteredCustomerDiscountStream(f)
				.filter(cd -> cd.getStartDate().isEqual(getStartDateOfLatestDiscount(f))).collect(Collectors.toList());
	}

	private Stream<Discount> getLatestFamilyFilteredCustomerDiscountStream(ItemFamily family) {
		return getLatestCustomerDiscountStream().filter(cd -> Util.areEqual(cd.getFamilyLimit(), family));
	}

	private Optional<Price> getOptionalPrice(Item item, PricingType pricingType) throws Exception {
		return item.getPriceList().stream()
				.filter(p -> p.getType().equals(pricingType) && p.getStartDate().compareTo(getOrderDate()) <= 0)
				.max(Price::compareTo);
	}

	private LocalDate getStartDateOfLatestDiscount(ItemFamily family) {
		try {
			return getLatestFamilyFilteredCustomerDiscountStream(family)
					.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get().getStartDate();
		} catch (Exception e) {
			return null;
		}
	}

	private String getTotalInText(BigDecimal t) {
		return "[TOTAL] " + formatCurrency(t);
	}

	private BigDecimal getVatDivisor() throws Exception {
		if (vatDivisor == null)
			setVatDivisor(ONE.add(vatService.vatRate()));
		return vatDivisor;
	}

	private BigDecimal getVolumeDiscountOfSetTypePrice(BigDecimal qty) {
		BigDecimal discount = ZERO;
		volumeDiscounts.sort(compareSetTypeVolumeDiscountChannelLimits());
		for (VolumeDiscount vd : volumeDiscounts)
			if (areChannelLimitsEqual(vd) || isAnAllChannelVolumeDiscount(vd)) {
				BigDecimal discountSet = qty.divideToIntegralValue(getCutOff(vd));
				discount = discountSet.multiply(vd.getDiscount());
				break;
			}
		BigDecimal net = qty.multiply(unitPrice).subtract(discount);
		return net.divide(qty, 8, HALF_EVEN);
	}

	private BigDecimal getVolumeDiscountOfTierTypePrice(BigDecimal qty) {
		BigDecimal unitDiscount = ZERO;
		volumeDiscounts.sort(compareTierTypeVolumeDiscountChannelLimitsThenReverseCutOff());
		for (VolumeDiscount vd : volumeDiscounts)
			if (getCutOff(vd).compareTo(qty) <= 0 && (areChannelLimitsEqual(vd) || isAnAllChannelVolumeDiscount(vd))) {
				unitDiscount = vd.getDiscount().divide(getQtyPerUom(vd.getUom()), 8, RoundingMode.HALF_EVEN);
				break;
			}
		return unitPrice.subtract(unitDiscount);
	}

	private boolean isAnAllChannelVolumeDiscount(VolumeDiscount vd) {
		return vd.getChannelLimit() == null;
	}

	private List<Long> listDiscountIds() {
		return discounts.stream().map(d -> d.getId()).collect(toList());
	}

	private List<String> listDiscounts() {
		if (isZero(getDiscountValue()))
			return null;
		ArrayList<String> list = new ArrayList<>();
		if (discounts.size() > 1)
			list.add(getTotalInText(getDiscountValue()));
		return getEachLevelDiscountTextList(list);
	}

	private Integer ordinal(ItemFamily b) {
		return valueOf(b.getTier().ordinal());
	}

	private int reverseCompareCutOffsWhenChannelLimitsAreEqual(VolumeDiscount a, VolumeDiscount b) {
		int comp = compareVolumeDiscountChannelLimits(a, b);
		return comp != 0 ? comp : valueOf(b.getCutoff()).compareTo(valueOf(a.getCutoff()));
	}

	private void setVolumeDiscounts() {
		List<VolumeDiscount> list = item.getVolumeDiscounts();
		LocalDate date = list.stream().filter(vd -> !vd.getStartDate().isAfter(getOrderDate()))
				.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get().getStartDate();
		volumeDiscounts = list.stream().filter(vd -> vd.getStartDate().isEqual(date)).collect(Collectors.toList());
	}

	private void updateTotals() {
		get().setGrossValue(computeGross());
		get().setTotalValue(computeTotal());
		get().setDiscounts(listDiscounts());
	}

	private Item validateItem(Long id) throws Exception {
		Item item = confirmItemExists(id);
		confirmItemIsNotOnList(item);
		confirmItemIsAllowedToBeSoldToCurrentCustomer(item);
		confirmItemDiscountEqualsCurrent(item);
		return item;
	}

	protected void setOrderDateAfterReset(LocalDate date) {
		reset();
		get().setOrderDate(date);
	}

	BigDecimal computeDiscountedPrice(BigDecimal qty) {
		if (item.getLatestVolumeDiscount(getOrderDate()) == null)
			return unitPrice;
		setVolumeDiscounts();
		if (volumeDiscounts.get(0).getType() == VolumeDiscountType.TIER)
			return getVolumeDiscountOfTierTypePrice(qty);
		return getVolumeDiscountOfSetTypePrice(qty);
	}

	Item getItem() {
		return item;
	}

	LocalDate getOrderDate() {
		return get().getOrderDate();
	}

	BigDecimal getQtyPerUom(UomType uom) {
		for (QtyPerUom qpu : item.getQtyPerUomList())
			if (qpu.getUom() == uom)
				return qpu.getQty();
		return null;
	}

	BigDecimal getUnitPrice() {
		return unitPrice;
	}

	boolean isVolumeDiscounted(Item item) {
		return item.getVolumeDiscounts() != null;
	}

	void setCustomer(Customer customer) {
		this.customer = customer;
	}

	void setItem(Item item) {
		this.item = item;
	}

	void setLatestPrice(Item item) throws Exception {
		unitPrice = null;
		Optional<Price> optPrice = getOptionalPrice(item, customer.getPrimaryPricingType());
		if (!optPrice.isPresent() && customer.getAlternatePricingType() != null)
			optPrice = getOptionalPrice(item, customer.getAlternatePricingType());
		unitPrice = optPrice.get().getPriceValue();
	}

	void setSellingUoms(List<UomType> sellingUoms) {
		this.sellingUoms = sellingUoms;
	}

	void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	void setVatDivisor(BigDecimal vatDivisor) {
		this.vatDivisor = vatDivisor;
	}
}
