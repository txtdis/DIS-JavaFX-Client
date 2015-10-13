package ph.txtdis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.NoArgsConstructor;
import ph.txtdis.dto.AbstractSoldOrder;
import ph.txtdis.dto.Audited;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.Discount;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.dto.SoldDetail;
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.DifferentDiscountException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.NotAnItemToBeSoldToCustomerException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;
import ph.txtdis.util.Numeric;
import ph.txtdis.util.Text;
import ph.txtdis.util.Util;

@NoArgsConstructor
public abstract class SoldService<T extends AbstractSoldOrder<PK>, PK>
		implements Audited, Serviced<T, PK>, SpunById<PK>
{

	@Autowired
	private ItemFamilyService familyService;

	@Autowired
	private ItemService itemService;

	@Autowired
	protected ReadOnlyService<T> readOnlyService;

	@Autowired
	private SavingService<T> savingService;

	@Autowired
	private SpunService<T, PK> spunService;

	@Autowired
	private VatService vatService;

	private BigDecimal unitPrice, vatDivisor;

	private Item item;

	private List<UomType> sellingUoms;

	private List<VolumeDiscount> volumeDiscounts;

	private String module;

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
			throw new DuplicateException("ID No. " + id);
	}

	public SoldDetail createDetail(UomType uom, BigDecimal qty, QualityType quality) {
		SoldDetail sd = new SoldDetail();
		sd.setItem(item);
		sd.setUom(uom);
		sd.setQty(qty);
		sd.setQuality(quality);
		sd.setPriceValue(computeUnitPrice(uom, qty));
		return sd;
	}

	@Override
	public T find(String id) throws Exception {
		T e = readOnlyService.module(getModule()).getOne("/" + id);
		if (e == null)
			throw new NotFoundException("ID No. " + id);
		return e;
	}

	@Override
	public T get() {
		return entity;
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
		if (get().getCredit() == null && isNew())
			get().setCredit(getLatestCredit());
		return get().getCredit();
	}

	public String getCustomerAddress() {
		return getCustomer() == null ? null : getCustomer().getAddress();
	}

	public Long getCustomerId() {
		return getCustomer() == null ? null : getCustomer().getId();
	}

	public String getCustomerName() {
		return getCustomer() == null ? null : getCustomer().getName();
	}

	public List<SoldDetail> getDetails() {
		if (get().getDetails() == null)
			setDetails(Collections.emptyList());
		return get().getDetails();
	}

	public List<String> getDiscountTextList() {
		if (getDiscounts().isEmpty())
			return Collections.emptyList();
		if (getDiscounts().size() == 1)
			return showTheOnlyDiscountLevel();
		return showTotalAndEachLevelDiscounts();
	}

	public BigDecimal getDiscountValue() {
		BigDecimal discountValue = BigDecimal.ZERO;
		BigDecimal grossValue = getGrossValue();
		for (Discount d : getDiscounts()) {
			discountValue = discountValue.add(grossValue.multiply(Numeric.toPercentRate(d.getPercent())));
			grossValue = getGrossValue().subtract(discountValue);
		}
		return discountValue;
	}

	public LocalDate getDueDate() {
		return getCustomer() == null ? null : getOrderDate().plusDays(creditTermsInDays());
	}

	@Override
	public PK getId() {
		return get().getId();
	}

	public String getItemDescription() {
		return item == null ? null : item.getDescription();
	}

	public List<UomType> getSellingUoms() throws Exception {
		if (sellingUoms == null)
			setSellingUoms(itemService.listSellingUoms(item));
		return sellingUoms;
	}

	@Override
	public PK getSpunId() {
		return isNew() ? null : getId();
	}

	public BigDecimal getTotalValue() {
		return getGrossValue() == BigDecimal.ZERO ? null : getGrossValue().subtract(getDiscountValue());
	}

	public BigDecimal getVatableValue() throws Exception {
		return getTotalValue() == null ? null : Numeric.divide(getTotalValue(), getVatDivisor());
	}

	public BigDecimal getVatValue() throws Exception {
		return getTotalValue() == null ? null : getTotalValue().subtract(getVatableValue());
	}

	@Override
	public boolean isNew() {
		return getCreatedBy() == null;
	}

	@Override
	public void next() throws Exception {
		set(spunService.module(getModule()).next(getSpunId()));
	}

	@Override
	public void previous() throws Exception {
		set(spunService.module(getModule()).previous(getSpunId()));
	}

	@Override
	public void save() throws Exception, SuccessfulSaveInfo {
		set(savingService.module(getModule()).save(entity));
		throw new SuccessfulSaveInfo(get());
	}

	@Override
	public void set(T entity) {
		if (entity != null)
			this.entity = entity;
	}

	public void setDetails(List<SoldDetail> details) {
		get().setDetails(details);
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
		get().setOrderDate(date);
	}

	private boolean areChannelLimitsEqual(VolumeDiscount vd) {
		return Util.areEqual(vd.getChannelLimit(), getCustomer().getChannel());
	}

	private int compareVolumeDiscountChannelLimits(VolumeDiscount a, VolumeDiscount b) {
		Channel aChannel = a.getChannelLimit();
		Channel bChannel = b.getChannelLimit();
		if (aChannel == null)
			return bChannel == null ? 0 : 1;
		return bChannel == null ? -1 : aChannel.compareTo(bChannel);
	}

	private Comparator<VolumeDiscount> compareVolumeDiscountOfSetTypeChannelLimits() {
		return (a, b) -> compareVolumeDiscountChannelLimits(a, b);
	}

	private Comparator<VolumeDiscount> compareVolumeDiscountOfTierTypeChannelLimitsThenReverseCutOff() {
		return (a, b) -> reverseCompareCutOffsWhenChannelLimitsAreEqual(a, b);
	}

	private BigDecimal computeUnitPrice(UomType uom, BigDecimal qty) {
		BigDecimal qtyPerUom = getQtyPerUom(uom);
		BigDecimal discountedPrice = computeDiscountedPrice(qty.multiply(qtyPerUom));
		return discountedPrice.multiply(qtyPerUom);
	}

	private void confirmItemDiscountEqualsCurrent(List<ItemFamily> families) throws Exception {
		List<Discount> discounts = getLatestDiscount(families);
		if (getDetails().isEmpty())
			get().setDiscounts(discounts);
		else if (!getDiscounts().equals(discounts))
			throw new DifferentDiscountException();
	}

	private Item confirmItemExists(Long id) throws Exception {
		return itemService.find(id);
	}

	private void confirmItemIsAllowedToBeSoldToCurrentCustomer(Item item) throws Exception {
		try {
			setLatestPrice(item);
		} catch (Exception e) {
			throw new NotAnItemToBeSoldToCustomerException(item, getCustomer());
		}
	}

	private void confirmItemIsNotOnList(Item item) throws Exception {
		if (getDetails().stream().filter(d -> d.getItem().equals(item)).findAny().isPresent())
			throw new DuplicateException(item.getName());
	}

	private Discount createDiscount(CustomerDiscount cd) {
		Discount discount = new Discount();
		discount.setLevel(cd.getLevel());
		discount.setPercent(cd.getPercent());
		return discount;
	}

	private Long creditTermsInDays() {
		return getCredit() == null ? 0L : getCredit().getTermInDays();
	}

	private Customer getCustomer() {
		return get().getCustomer();
	}

	private BigDecimal getCutOff(VolumeDiscount vd) {
		BigDecimal cutOff = new BigDecimal(vd.getCutOff());
		BigDecimal qtyPerUom = getQtyPerUom(vd.getUom());
		return cutOff.multiply(qtyPerUom);
	}

	private List<Discount> getDiscounts() {
		if (get().getDiscounts() == null)
			get().setDiscounts(Collections.emptyList());
		return get().getDiscounts();
	}

	private List<ItemFamily> getFamilies(Item item) throws Exception {
		return familyService.getItemAncestry(item);
	}

	private BigDecimal getGrossValue() {
		return getDetails().isEmpty() ? BigDecimal.ZERO
				: getDetails().stream().map(d -> d.getSubtotalValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	private ItemFamily getHighestTierFamilyAmongDiscountLimits(List<ItemFamily> families) {
		try {
			return getLatestCustomerDiscountStream().filter(cd -> families.contains(cd.getFamilyLimit()))
					.map(cd -> cd.getFamilyLimit()).min(ItemFamily::compareTo).get();
		} catch (Exception e) {
			return null;
		}
	}

	private CreditDetail getLatestCredit() {
		try {
			return getCustomer().getCreditDetails().stream()
					.filter(p -> p.getStartDate().compareTo(getOrderDate()) <= 0).max(CreditDetail::compareTo).get();
		} catch (Exception e) {
			return null;
		}
	}

	private Stream<CustomerDiscount> getLatestCustomerDiscountStream() {
		try {
			return getCustomer().getDiscounts().stream().filter(cd -> cd.getStartDate().compareTo(getOrderDate()) <= 0);
		} catch (Exception e) {
			return Stream.empty();
		}
	}

	private List<Discount> getLatestDiscount(ItemFamily family) {
		return getLatestFamilyFilteredCustomerDiscountStream(family)
				.filter(cd -> cd.getStartDate().isEqual(getStartDateOfLatestDiscount(family)))
				.map(cd -> createDiscount(cd)).collect(Collectors.toList());
	}

	private Stream<CustomerDiscount> getLatestFamilyFilteredCustomerDiscountStream(ItemFamily family) {
		return getLatestCustomerDiscountStream().filter(cd -> Util.areEqual(cd.getFamilyLimit(), family));
	}

	private Optional<Price> getOptionalPrice(Item item, PricingType pricingType) throws Exception {
		return item.getPriceList().stream()
				.filter(p -> p.getType().equals(pricingType) && p.getStartDate().compareTo(getOrderDate()) <= 0)
				.max(Price::compareTo);
	}

	private PricingType getPricingAlternateType() {
		return getCustomer() == null ? null : getCustomer().getAlternatePricingType();
	}

	private PricingType getPricingPrimaryType() {
		return getCustomer() == null ? null : getCustomer().getPrimaryPricingType();
	}

	private LocalDate getStartDateOfLatestDiscount(ItemFamily family) {
		try {
			return getLatestFamilyFilteredCustomerDiscountStream(family).max(CustomerDiscount::compareTo).get()
					.getStartDate();
		} catch (Exception e) {
			return null;
		}
	}

	private String getTotalDiscountInText() {
		return Numeric.formatCurrency(getDiscountValue());
	}

	private BigDecimal getVatDivisor() throws Exception {
		if (vatDivisor == null)
			setVatDivisor(BigDecimal.ONE.add(vatService.vatRate()));
		return vatDivisor;
	}

	private BigDecimal getVolumeDiscountOfSetTypePrice(BigDecimal qty) {
		BigDecimal discount = BigDecimal.ZERO;
		volumeDiscounts.sort(compareVolumeDiscountOfSetTypeChannelLimits());
		for (VolumeDiscount vd : volumeDiscounts)
			if (areChannelLimitsEqual(vd) || isAnAllChannelVolumeDiscount(vd)) {
				BigDecimal discountSet = qty.divideToIntegralValue(getCutOff(vd));
				discount = discountSet.multiply(vd.getDiscount());
				break;
			}
		BigDecimal net = qty.multiply(unitPrice).subtract(discount);
		return net.divide(qty, 8, RoundingMode.HALF_EVEN);
	}

	private BigDecimal getVolumeDiscountOfTierTypePrice(BigDecimal qty) {
		BigDecimal unitDiscount = BigDecimal.ZERO;
		volumeDiscounts.sort(compareVolumeDiscountOfTierTypeChannelLimitsThenReverseCutOff());
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

	private boolean isAnAllItemDiscount() {
		return getLatestFamilyFilteredCustomerDiscountStream(null).findAny().isPresent();
	}

	private int reverseCompareCutOffsWhenChannelLimitsAreEqual(VolumeDiscount a, VolumeDiscount b) {
		int comp = compareVolumeDiscountChannelLimits(a, b);
		return comp != 0 ? comp : Integer.valueOf(b.getCutOff()).compareTo(Integer.valueOf(a.getCutOff()));
	}

	private void setVolumeDiscounts() {
		List<VolumeDiscount> list = item.getVolumeDiscounts();
		LocalDate date = list.stream().filter(vd -> !vd.getStartDate().isAfter(getOrderDate()))
				.max(VolumeDiscount::compareTo).get().getStartDate();
		volumeDiscounts = list.stream().filter(vd -> vd.getStartDate().isEqual(date)).collect(Collectors.toList());
	}

	private List<String> showTheOnlyDiscountLevel() {
		return Arrays.asList("[" + getDiscounts().get(0).getPercent() + "%] " + getTotalDiscountInText());
	}

	private List<String> showTotalAndEachLevelDiscounts() {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal net = getGrossValue();
		ArrayList<String> list = new ArrayList<>(Arrays.asList("[TOTAL] " + getTotalDiscountInText()));
		for (Discount discount : getDiscounts()) {
			BigDecimal perLevel = net.multiply(Numeric.toPercentRate(discount.getPercent()));
			total = total.add(perLevel);
			net = net.subtract(total);
			list.add("[" + discount.getLevel() + "- " + discount.getPercent() + "%] "
					+ Numeric.formatCurrency(perLevel));
		}
		return list;
	}

	private Item validateItem(Long id) throws Exception {
		Item item = confirmItemExists(id);
		confirmItemIsNotOnList(item);
		confirmItemIsAllowedToBeSoldToCurrentCustomer(item);
		confirmItemDiscountEqualsCurrent(getFamilies(item));
		return item;
	}

	BigDecimal computeDiscountedPrice(BigDecimal qty) {
		if (item.getVolumeDiscounts() == null)
			return unitPrice;
		setVolumeDiscounts();
		if (volumeDiscounts.get(0).getType() == VolumeDiscountType.TIER)
			return getVolumeDiscountOfTierTypePrice(qty);
		return getVolumeDiscountOfSetTypePrice(qty);
	}

	Item getItem() {
		return item;
	}

	List<Discount> getLatestDiscount(List<ItemFamily> families) {
		ItemFamily family = getHighestTierFamilyAmongDiscountLimits(families);
		if (!(family == null && !isAnAllItemDiscount()))
			return getLatestDiscount(family);
		return Collections.emptyList();
	}

	String getModule() {
		if (module == null)
			module = Text.nameFromService(this);
		return module;
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

	void setItem(Item item) {
		this.item = item;
	}

	void setLatestPrice(Item item) throws Exception {
		unitPrice = null;
		Optional<Price> optPrice = getOptionalPrice(item, getPricingPrimaryType());
		if (!optPrice.isPresent() && getPricingAlternateType() != null)
			optPrice = getOptionalPrice(item, getPricingAlternateType());
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
