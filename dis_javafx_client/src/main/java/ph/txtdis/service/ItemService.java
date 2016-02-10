package ph.txtdis.service;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.log4j.Logger.getLogger;
import static org.springframework.util.StringUtils.capitalize;
import static ph.txtdis.type.ItemType.PURCHASED;
import static ph.txtdis.type.ScriptType.PRICE_APPROVAL;
import static ph.txtdis.type.ScriptType.VOLUME_DISCOUNT_APPROVAL;
import static ph.txtdis.type.UomType.PC;
import static ph.txtdis.type.UomType.values;
import static ph.txtdis.type.UserType.MANAGER;
import static ph.txtdis.util.SpringUtil.isUser;
import static ph.txtdis.util.SpringUtil.username;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;
import static ph.txtdis.util.DateTimeUtils.toHypenatedYearMonthDay;
import static ph.txtdis.util.DateTimeUtils.validateDateIsNotInThePast;
import static ph.txtdis.util.DateTimeUtils.validateStartDate;

import ph.txtdis.dto.Bom;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.EntityDecisionNeeded;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.AlreadyBilledBookingException;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.DeactivatedException;
import ph.txtdis.exception.DifferentDiscountException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.NoVendorIdPurchasedItemException;
import ph.txtdis.exception.NotAllowedOffSiteTransactionException;
import ph.txtdis.exception.NotAnItemToBeSoldToCustomerException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.exception.ToBeReturnedItemNotPurchasedWithinTheLastSixMonths;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.ItemType;
import ph.txtdis.type.ScriptType;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;
import ph.txtdis.util.ServerUtil;

@Service("itemService")
public class ItemService implements ByNameSearchable<Item>, ChannelLimited, DecisionNeeded, Excel<Item>, Reset,
		ItemBased<Bom>, Serviced<Long>, ServiceDeactivated<Long>
{

	private class NotAPurchasedItemException extends Exception {

		private static final long serialVersionUID = -2318450396396173273L;

		public NotAPurchasedItemException(Item item) {
			super(item + "\nis NOT a purchased item.");
		}
	}

	private class NotToBeSoldItemException extends Exception {

		private static final long serialVersionUID = -2318450396396173273L;

		public NotToBeSoldItemException(Item item) {
			super(item + "\nis NOT to be sold.");
		}
	}

	private static Logger logger = getLogger(ItemService.class);

	private static final String DISCOUNT_TAB = "Volume Discount";

	private static final String PRICING_TAB = "Pricing";

	private static final String ITEM = "item";

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ItemFamilyService itemFamilyService;

	@Autowired
	private PricingTypeService pricingTypeService;

	@Autowired
	private ReadOnlyService<Item> readOnlyService;

	@Autowired
	private SavingService<Item> savingService;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private SpunService<Item, Long> spunService;

	@Autowired
	private ServerUtil server;

	private List<Item> items;

	private Item item, part;

	private String tab;

	public ItemService() {
		reset();
	}

	@Override
	public boolean canApprove() {
		return isUser(MANAGER);
	}

	public Bom createBom(BigDecimal qty) {
		Bom b = new Bom();
		b.setPart(part);
		b.setQty(qty);
		return b;
	}

	public VolumeDiscount createDiscountUponValidation(VolumeDiscountType type, UomType uom, Integer cutoff,
			BigDecimal discount, Channel channel, LocalDate startDate)
					throws DateInThePastException, DuplicateException {
		validateStartDate(discounts(), startDate);
		return new VolumeDiscount(type, uom, cutoff, discount, nullIfAll(channel), startDate);
	}

	public Price createPricingUponValidation(PricingType type, BigDecimal price, Channel channel, LocalDate startDate)
			throws DateInThePastException, DuplicateException {
		validateStartDateOfPricingType(prices(), type, startDate);
		return new Price(type, price, nullIfAll(channel), startDate);
	}

	public QtyPerUom createQtyPerUom(UomType uom, BigDecimal qty, Boolean isPurchased, Boolean isSold,
			Boolean isReported) {
		QtyPerUom q = new QtyPerUom();
		q.setUom(uom);
		q.setQty(qty);
		q.setPurchased(isPurchased);
		q.setSold(isSold);
		q.setReported(isReported);
		return q;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Item find(Long id) throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException, NotFoundException, DeactivatedException, NoVendorIdPurchasedItemException, RestException {
		Item i = readOnlyService.module(ITEM).getOne("/" + id);
		if (i == null)
			throw new NotFoundException("Item No. " + id);
		if (i.getDeactivatedOn() != null)
			throw new DeactivatedException(i.getName());
		if (i.getType() == PURCHASED && i.getVendorId() == null)
			throw new NoVendorIdPurchasedItemException(i);
		return i;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Item get() {
		if (item == null)
			reset();
		return item;
	}

	@Override
	public String getAlternateName() {
		return capitalize(ITEM);
	}

	@Override
	public ChannelService getChannelService() {
		return channelService;
	}

	@Override
	public String getCreatedBy() {
		return get().getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return get().getCreatedOn();
	}

	@Override
	public String getDecidedBy() {
		return username();
	}

	@Override
	public ZonedDateTime getDecidedOn() {
		return ZonedDateTime.now();
	}

	@Override
	public List<Bom> getDetails() {
		logger.info("listBOMs @ getDetails = " + listBoms());
		return listBoms() == null ? emptyList() : listBoms();
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	@Override
	public Boolean getIsValid() {
		return getDeactivatedOn() != null;
	}

	@Override
	public Item getItem() {
		return part;
	}

	@Override
	public ItemService getItemService() {
		return this;
	}

	@Override
	public String getModule() {
		return ITEM;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<Item> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public String getRemarks() {
		return "";
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<Item> getSavingService() {
		return savingService;
	}

	@Override
	public ScriptService getScriptService() {
		return scriptService;
	}

	@Override
	public <T extends EntityDecisionNeeded<Long>> ScriptType getScriptType(T d) {
		return (d instanceof Price) ? PRICE_APPROVAL : VOLUME_DISCOUNT_APPROVAL;
	}

	@Override
	public SpunService<Item, Long> getSpunService() {
		return spunService;
	}

	public boolean hasPurchaseUom() {
		return uomExists(q -> q.getPurchased() != null && q.getPurchased() == true);
	}

	public boolean hasReportUom() {
		return uomExists(q -> q.getReported() != null && q.getReported() == true);
	}

	public boolean isPurchased() {
		return get().getType() == PURCHASED;
	}

	@Override
	public List<Item> list() {
		return items;
	}

	public List<Bom> listBoms() {
		logger.info("get().getBoms @ listBoms() = " + get().getBoms());
		return get().getBoms();
	}

	public List<UomType> listBuyingUoms(Item item) throws NotAPurchasedItemException {
		List<UomType> uom = filterBoughtUom(item);
		if (uom.isEmpty())
			throw new NotAPurchasedItemException(item);
		return uom;
	}

	public List<ItemFamily> listParents() {
		if (isNew())
			try {
				return itemFamilyService.listItemParents();
			} catch (Exception e) {
				return null;
			}
		return asList(get().getFamily());
	}

	public List<PricingType> listPricingTypes() {
		try {
			return pricingTypeService.list();
		} catch (Exception e) {
			return null;
		}
	}

	public List<QtyPerUom> listQtyPerUom() {
		return get().getQtyPerUomList();
	}

	public List<UomType> listSellingUoms() {
		try {
			return listSellingUoms(item);
		} catch (NotToBeSoldItemException e) {
			return null;
		}
	}

	public List<UomType> listSellingUoms(Item item) throws NotToBeSoldItemException {
		List<UomType> uom = filterSoldUom(item);
		if (uom.isEmpty())
			throw new NotToBeSoldItemException(item);
		return uom;
	}

	public List<ItemType> listTypes() {
		return asList(ItemType.values());
	}

	public List<UomType> listUoms() {
		return listQtyPerUom() == null ? asList(PC) : unusedUoms();
	}

	public boolean noChangesNeedingApproval(String tab) {
		this.tab = tab;
		if (tab.equals(PRICING_TAB))
			return noChanges(get().getPriceList());
		if (tab.equals(DISCOUNT_TAB))
			return noChanges(get().getVolumeDiscounts());
		return true;
	}

	@Override
	public void reset() {
		set(new Item());
		items = null;
		part = null;
	}

	@Override
	public void save() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		scriptService.saveScripts();
		Serviced.super.save();
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	@Override
	public List<Item> search(String text) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		String endpoint = text.isEmpty() ? "" : "/search?name=" + text;
		return items = readOnlyService.module(ITEM).getList(endpoint);
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		item = (Item) t;
	}

	public void setNameIfUnique(String text)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			DuplicateException, RestException, NotAllowedOffSiteTransactionException {
		if (server.isOffSite())
			throw new NotAllowedOffSiteTransactionException();
		if (readOnlyService.module(getModule()).getOne("/find?name=" + text) != null)
			throw new DuplicateException(text);
		get().setName(text);
	}

	public void setPartUponValidation(Long id)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			NotFoundException, DuplicateException, NotAnItemToBeSoldToCustomerException, DifferentDiscountException,
			DeactivatedException, NoVendorIdPurchasedItemException, RestException,
			ToBeReturnedItemNotPurchasedWithinTheLastSixMonths, AlreadyBilledBookingException {
		if (id != 0) {
			part = null;
			part = verifyItem(id);
		}
	}

	@Override
	public void updatePerValidity(Boolean isValid, String remarks) {
		if (tab.equals(PRICING_TAB))
			approvePricing(isValid, remarks);
		if (tab.equals(DISCOUNT_TAB))
			approveDiscount(isValid, remarks);
	}

	public void validatePurchasedUom(Boolean b) throws DuplicateException {
		if (isNewQtyPerUom())
			return;
		if (hasPurchaseUom())
			throw new DuplicateException("A purchase UOM");
	}

	public void validateReportedUom(Boolean value) throws DuplicateException {
		if (isNewQtyPerUom())
			return;
		if (hasReportUom())
			throw new DuplicateException("A report UOM");
	}

	private void approveDiscount(Boolean isValid, String remarks) {
		List<VolumeDiscount> list = approve(get().getVolumeDiscounts(), isValid, remarks);
		get().setVolumeDiscounts(list);
	}

	private void approvePricing(Boolean isValid, String remarks) {
		List<Price> l = approve(get().getPriceList(), isValid, remarks);
		get().setPriceList(l);
	}

	private List<VolumeDiscount> discounts() {
		if (get().getVolumeDiscounts() == null)
			get().setVolumeDiscounts(new ArrayList<>());
		return get().getVolumeDiscounts();
	}

	private List<UomType> filterBoughtUom(Item item) {
		List<QtyPerUom> qpu = item.getQtyPerUomList();
		return qpu == null ? new ArrayList<>()
				: qpu.stream().filter(q -> q.getPurchased() != null && q.getPurchased() == true).map(q -> q.getUom())
						.collect(toList());
	}

	private List<UomType> filterSoldUom(Item item) {
		List<QtyPerUom> qpu = item.getQtyPerUomList();
		return qpu == null ? new ArrayList<>()
				: qpu.stream().filter(q -> q.getSold() != null).map(q -> q.getUom()).collect(toList());
	}

	private String getExcelFileName() {
		return getExcelSheetName() + "." + toHypenatedYearMonthDay(now());
	}

	private String getExcelSheetName() {
		return "Active.Items";
	}

	private boolean isNewQtyPerUom() {
		return listQtyPerUom() == null || listQtyPerUom().isEmpty();
	}

	private <T extends EntityDecisionNeeded<Long>> boolean noChanges(List<T> l) {
		return l == null ? true : !l.stream().anyMatch(d -> d.getIsValid() == null);
	}

	private List<Price> prices() {
		if (get().getPriceList() == null)
			get().setPriceList(new ArrayList<>());
		return get().getPriceList();
	}

	private boolean startDateForPaymentTypeExists(List<Price> list, PricingType type, LocalDate startDate) {
		return list.stream().anyMatch(r -> r.getStartDate().equals(startDate) && r.getType().equals(type));
	}

	private List<UomType> unusedUoms() {
		List<UomType> l = new ArrayList<>(asList(values()));
		l.removeAll(listQtyPerUom().stream().map(QtyPerUom::getUom).collect(toList()));
		return l;
	}

	private boolean uomExists(Predicate<QtyPerUom> p) {
		return listQtyPerUom().stream().anyMatch(p);
	}

	private void validateStartDateOfPricingType(List<Price> prices, PricingType type, LocalDate startDate)
			throws DateInThePastException, DuplicateException {
		validateDateIsNotInThePast(startDate);
		if (startDateForPaymentTypeExists(prices, type, startDate))
			throw new DuplicateException(toDateDisplay(startDate) + " for " + type);
	}
}
