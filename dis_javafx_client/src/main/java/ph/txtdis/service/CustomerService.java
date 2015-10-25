package ph.txtdis.service;

import static ph.txtdis.type.ItemTier.PRINCIPAL;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Discount;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.Routing;
import ph.txtdis.dto.StartDated;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.CustomerType;
import ph.txtdis.type.VisitFrequency;
import ph.txtdis.util.Spring;
import ph.txtdis.util.Temporal;

@Service
public class CustomerService
		implements AlternateNamed, Excel<Customer>, Reset, SpunById<Long>, Serviced<Customer, Long>
{

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ItemFamilyService familyService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private ReadOnlyService<Customer> readOnlyService;

	@Autowired
	private RouteService routeService;

	@Autowired
	private SavingService<Customer> savingService;

	@Autowired
	private SpunService<Customer, Long> spunService;

	@Autowired
	private ExcelWriter excel;

	private Customer customer;

	private List<Customer> customers;

	private ItemFamily allItemFamilies;

	public CustomerService() {
		reset();
	}

	public CreditDetail createCreditLineUponValidation(int term, int gracePeriod, BigDecimal creditLimit,
			LocalDate startDate) throws Exception {
		validateStartDate(creditDetails(), startDate);
		return createCreditLine(term, gracePeriod, creditLimit, startDate);
	}

	public Discount createDiscountUponValidation(int level, BigDecimal percent, ItemFamily family,
			LocalDate startDate) throws Exception {
		validateStartDate(discounts(), startDate);
		return createCustomerDiscount(level, percent, familyLimit(family), startDate);
	}

	public Routing createRouteAssignmentUponValidation(Route route, LocalDate startDate) throws Exception {
		validateStartDate(routeHistory(), startDate);
		return createRouteAssignment(route, startDate);
	}

	public void deactivate() throws Exception, SuccessfulSaveInfo {
		get().setDeactivatedBy(Spring.username());
		get().setDeactivatedOn(ZonedDateTime.now());
		save();
	}

	@Override
	public Customer find(String id) throws Exception {
		Customer c = readOnlyService.module(getModule()).getOne("/" + id);
		if (c == null)
			throw new NotFoundException("Customer No. " + id);
		return c;
	}

	@Override
	public Customer get() {
		if (customer == null)
			reset();
		return customer;
	}

	public String getAddress() {
		return get().getAddress();
	}

	@Override
	public String getAlternateName() {
		return StringUtils.capitalize(getModule());
	}

	public Location getBarangay() {
		return get().getBarangay();
	}

	public Channel getChannel() {
		return get().getChannel();
	}

	public Location getCity() {
		return get().getCity();
	}

	public String getContactTitle() {
		return get().getContactTitle();
	}

	@Override
	public String getCreatedBy() {
		return get().getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return get().getCreatedOn();
	}

	public String getCreditContactName() {
		return get().getContactName();
	}

	public String getCreditContactSurname() {
		return get().getContactSurname();
	}

	public List<CreditDetail> getCreditDetails() {
		return get().getCreditDetails();
	}

	public String getDeactivatedBy() {
		return get().getDeactivatedBy();
	}

	public ZonedDateTime getDeactivatedOn() {
		return get().getDeactivatedOn();
	}

	public List<Discount> getDiscounts() {
		return get().getDiscounts();
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	public String getMobile() {
		return get().getMobile();
	}

	@Override
	public String getModule() {
		return "customer";
	}

	public String getName() {
		return get().getName();
	}

	public Long getParentId() {
		return getParent() == null ? null : getParent().getId();
	}

	public String getParentName() {
		return getParent() == null ? null : getParent().getName();
	}

	public Location getProvince() {
		return get().getProvince();
	}

	public List<Routing> getRouteHistory() {
		return get().getRouteHistory();
	}

	@Override
	public SavingService<Customer> getSavingService() {
		return savingService;
	}

	@Override
	public SpunService<Customer, Long> getSpunService() {
		return spunService;
	}

	public String getStreet() {
		return get().getStreet();
	}

	public CustomerType getType() {
		return get().getType();
	}

	public VisitFrequency getVisitFrequency() {
		return get().getVisitFrequency();
	}

	@Override
	public List<Customer> list() throws Exception {
		return customers;
	}

	public List<ItemFamily> listAllFamilies() throws Exception {
		List<ItemFamily> list = new ArrayList<>();
		list.add(getAllItemFamilies());
		list.addAll(familyService.list());
		return list;
	}

	public List<Location> listBarangays(Location city) throws Exception {
		return locationService.listBarangays(city);
	}

	public List<Channel> listChannels() throws Exception {
		return channelService.list();
	}

	public List<Location> listCities(Location province) throws Exception {
		return locationService.listCities(province);
	}

	public List<Location> listProvinces() throws Exception {
		return locationService.listProvinces();
	}

	public List<Route> listRoutes() throws Exception {
		return routeService.list();
	}

	@Override
	public void reset() {
		set(new Customer());
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws Exception {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	public List<Customer> search(String text) throws Exception {
		String endpoint = text.isEmpty() ? "" : "/find?name=" + text;
		return customers = readOnlyService.module(getModule()).getList(endpoint);
	}

	@Override
	public void set(Keyed<Long> customer) {
		this.customer = (Customer) customer;
	}

	public void setBarangay(Location value) {
		get().setBarangay(value);
	}

	public void setChannel(Channel value) {
		get().setChannel(value);
	}

	public void setCity(Location value) {
		get().setCity(value);
	}

	public void setContactTitle(String text) {
		get().setContactTitle(text);
	}

	public void setCreditContactName(String text) {
		get().setContactName(text);
	}

	public void setCreditContactSurname(String text) {
		get().setContactSurname(text);
	}

	public void setCreditDetails(List<CreditDetail> list) {
		get().setCreditDetails(list);
	}

	public void setDiscounts(List<Discount> list) {
		get().setDiscounts(list);
	}

	public void setMobile(String text) {
		get().setMobile(text);
	}

	public void setName(String text) {
		get().setName(text);
	}

	public void setNameIfUnique(String text) throws Exception {
		if (readOnlyService.module(getModule()).getOne("/find?name=" + text) != null)
			throw new DuplicateException(text);
		setName(text);
	}

	public void setParentIfExists(Long id) throws Exception {
		get().setParent(find(id.toString()));
	}

	public void setProvince(Location value) {
		get().setProvince(value);
	}

	public void setRouteHistory(List<Routing> items) {
		get().setRouteHistory(items);
	}

	public void setStreet(String text) {
		get().setStreet(text);
	}

	public void setType(CustomerType value) {
		get().setType(value);
	}

	public void setVisitFrequency(VisitFrequency value) {
		get().setVisitFrequency(value);
	}

	private CreditDetail createCreditLine(int term, int gracePeriod, BigDecimal creditLimit, LocalDate startDate) {
		CreditDetail credit = new CreditDetail();
		credit.setTermInDays(term);
		credit.setGracePeriodInDays(gracePeriod);
		credit.setCreditLimit(creditLimit);
		credit.setStartDate(startDate);
		updateCreditDetails(credit);
		return credit;
	}

	private Discount createCustomerDiscount(int level, BigDecimal percent, ItemFamily family,
			LocalDate startDate) {
		Discount discount = new Discount();
		discount.setLevel(level);
		discount.setPercent(percent);
		discount.setFamilyLimit(family);
		discount.setStartDate(startDate);
		updateCustomerDiscounts(discount);
		return discount;
	}

	private Routing createRouteAssignment(Route route, LocalDate startDate) {
		Routing routing = new Routing();
		routing.setRoute(route);
		routing.setStartDate(startDate);
		updateRouteHistory(routing);
		return routing;
	}

	private List<CreditDetail> creditDetails() {
		if (getCreditDetails() == null)
			setCreditDetails(new ArrayList<>());
		return getCreditDetails();
	}

	private List<Discount> discounts() {
		if (getDiscounts() == null)
			setDiscounts(new ArrayList<>());
		return getDiscounts();
	}

	private ItemFamily familyLimit(ItemFamily family) {
		return family.equals(allItemFamilies) ? null : family;
	}

	private ItemFamily getAllItemFamilies() {
		allItemFamilies = new ItemFamily();
		allItemFamilies.setName("ALL");
		allItemFamilies.setTier(PRINCIPAL);
		return allItemFamilies;
	}

	private String getExcelFileName() {
		return getExcelSheetName() + "." + Temporal.toFilename(getTimestamp());
	}

	private String getExcelSheetName() {
		return "Active.Customers";
	}

	private Customer getParent() {
		return get().getParent();
	}

	private LocalDate getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Routing> routeHistory() {
		if (getRouteHistory() == null)
			setRouteHistory(new ArrayList<>());
		return getRouteHistory();
	}

	private boolean startDateExists(List<? extends StartDated> list, LocalDate startDate) {
		return list.stream().filter(r -> r.getStartDate().equals(startDate)).findAny().isPresent();
	}

	private void updateCreditDetails(CreditDetail credit) {
		List<CreditDetail> list = new ArrayList<>(getCreditDetails());
		list.add(credit);
		setCreditDetails(list);
	}

	private void updateCustomerDiscounts(Discount discount) {
		List<Discount> list = new ArrayList<>(getDiscounts());
		list.add(discount);
		setDiscounts(list);
	}

	private void updateRouteHistory(Routing routing) {
		List<Routing> list = new ArrayList<>(getRouteHistory());
		list.add(routing);
		setRouteHistory(list);
	}

	private void validateDateIsNotInThePast(LocalDate startDate) throws Exception {
		if (startDate.isBefore(LocalDate.now()))
			throw new DateInThePastException();
	}

	private void validateDateIsUnique(List<? extends StartDated> list, LocalDate startDate) throws Exception {
		if (startDateExists(list, startDate))
			throw new DuplicateException(Temporal.format(startDate));
	}

	private void validateStartDate(List<? extends StartDated> list, LocalDate startDate) throws Exception {
		validateDateIsNotInThePast(startDate);
		validateDateIsUnique(list, startDate);
	}
}
