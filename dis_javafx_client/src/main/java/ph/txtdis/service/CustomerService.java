package ph.txtdis.service;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.springframework.util.StringUtils.capitalize;
import static ph.txtdis.type.ScriptType.CREDIT_APPROVAL;
import static ph.txtdis.type.ScriptType.CUSTOMER_DISCOUNT_APPROVAL;
import static ph.txtdis.type.UserType.MANAGER;
import static ph.txtdis.util.SpringUtil.isUser;
import static ph.txtdis.util.SpringUtil.username;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;
import static ph.txtdis.util.DateTimeUtils.toHypenatedYearMonthDay;
import static ph.txtdis.util.DateTimeUtils.validateDateIsNotInThePast;
import static ph.txtdis.util.DateTimeUtils.validateDateIsUnique;

import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.EntityDecisionNeeded;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.Routing;
import ph.txtdis.dto.StartDated;
import ph.txtdis.dto.WeeklyVisit;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.DeactivatedException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.NotAllowedOffSiteTransactionException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.PartnerType;
import ph.txtdis.type.ScriptType;
import ph.txtdis.type.VisitFrequency;
import ph.txtdis.util.NumberUtils;
import ph.txtdis.util.ServerUtil;

@Service("customerService")
public class CustomerService
		implements DecisionNeeded, Excel<Customer>, ItemFamilyLimited, Reset, Serviced<Long>, ServiceDeactivated<Long>
{

	private static final String DISCOUNT_TAB = "Customer Discount";

	private static final String CREDIT_TAB = "Credit Details";

	@Autowired
	private ChannelService channelService;

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ItemFamilyService familyService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private PricingTypeService pricingTypeService;

	@Autowired
	private ReadOnlyService<Customer> readOnlyService;

	@Autowired
	private RouteService routeService;

	@Autowired
	private SavingService<Customer> savingService;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private SpunService<Customer, Long> spunService;

	@Autowired
	private ServerUtil server;

	private Customer customer;

	private List<Customer> customers;

	private String tab;

	public CustomerService() {
		reset();
	}

	@Override
	public boolean canApprove() {
		return isUser(MANAGER);
	}

	public CreditDetail createCreditLineUponValidation(int term, int gracePeriod, BigDecimal creditLimit,
			LocalDate startDate) throws Exception {
		validateStartDate(creditDetails(), startDate);
		return createCreditLine(term, gracePeriod, creditLimit, startDate);
	}

	public CustomerDiscount createDiscountUponValidation(int level, BigDecimal percent, ItemFamily family,
			LocalDate startDate) throws Exception {
		validateStartDate(customerDiscounts(), startDate, level);
		return createCustomerDiscount(level, percent, nullIfAll(family), startDate);
	}

	public Routing createRouteAssignmentUponValidation(Route route, LocalDate startDate) throws Exception {
		validateStartDate(routeHistory(), startDate);
		return createRouteAssignment(route, startDate);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Customer find(String id) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, DeactivatedException, RestException {
		Customer c = Serviced.super.find(id);
		if (c.getDeactivatedOn() != null)
			throw new DeactivatedException(c.getName());
		return c;
	}

	public Customer findNoContactDetails() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noContactDetails");
	}

	public Customer findNoDesignation() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noDesignation");
	}

	public Customer findNoMobileNo() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noMobile");
	}

	public Customer findNoStreetAddress() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noStreetAddress");
	}

	public Customer findNoSurname() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noSurname");
	}

	public Customer findNotCorrectBarangayAddress() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/notCorrectBarangayAddress");
	}

	public Customer findNotCorrectCityAddress() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/notCorrectCityAddress");
	}

	public Customer findNotCorrectProvincialAddress() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/notCorrectProvincialAddress");
	}

	public Customer findNotTheSameVisitFrequencyAndSchedule() throws NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/notTheSameVisitFrequencyAndSchedule");
	}

	public Customer findNotTheSameWeeksOneAndFiveVisitSchedule() throws NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/notTheSameWeeksOneAndFiveVisitSchedules");
	}

	public Customer findNoVisitSchedule() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/noVisitSchedule");
	}

	@Override
	@SuppressWarnings("unchecked")
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
		return capitalize(getModule());
	}

	public List<Customer> getBanks() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getList("/banks");
	}

	public List<Channel> getChannels() {
		return isNew() ? listChannels() : asList(get().getChannel());
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
	public String getDeactivatedBy() {
		return get().getDeactivatedBy();
	}

	@Override
	public ZonedDateTime getDeactivatedOn() {
		return get().getDeactivatedOn();
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
	public Long getId() {
		return get().getId();
	}

	@Override
	public Boolean getIsValid() {
		return get().getDeactivatedOn() != null;
	}

	@Override
	public ItemFamilyService getItemFamilyService() {
		return familyService;
	}

	@Override
	public String getModule() {
		return "customer";
	}

	public Long getParentId() {
		return getParent() == null ? null : getParent().getId();
	}

	public String getParentName() {
		return getParent() == null ? null : getParent().getName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<Customer> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public String getRemarks() {
		return "";
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<Customer> getSavingService() {
		return savingService;
	}

	@Override
	public ScriptService getScriptService() {
		return scriptService;
	}

	@Override
	public <T extends EntityDecisionNeeded<Long>> ScriptType getScriptType(T d) {
		return (d instanceof CreditDetail) ? CREDIT_APPROVAL : CUSTOMER_DISCOUNT_APPROVAL;
	}

	@Override
	public SpunService<Customer, Long> getSpunService() {
		return spunService;
	}

	public List<PartnerType> getTypes() {
		return isNew() ? asList(PartnerType.values()) : asList(get().getType());
	}

	public Customer getVendor() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/vendor");
	}

	public List<WeeklyVisit> getVisitSchedule(Channel c) {
		return c != null && isAVisitedChannel(c) ? visitSchedule() : null;
	}

	@Override
	public List<Customer> list() {
		return customers;
	}

	public List<Location> listBarangays(Location city) {
		try {
			return locationService.listBarangays(city);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Location> listCities(Location province) {
		try {
			return locationService.listCities(province);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Location> listProvinces() {
		try {
			return locationService.listProvinces();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Route> listRoutes() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return routeService.list();
	}

	public List<Channel> listVisitedChannels() {
		return channelService.listVisitedChannels();
	}

	public boolean noChangesNeedingApproval(String tab) {
		this.tab = tab;
		if (tab.equals(CREDIT_TAB))
			return noChanges(get().getCreditDetails());
		if (tab.equals(DISCOUNT_TAB))
			return noChanges(get().getCustomerDiscounts());
		return true;
	}

	@Override
	public void reset() {
		set(new Customer());
		customers = null;
		tab = null;
	}

	@Override
	public void save() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		scriptService.saveScripts();
		PricingType type = pricingTypeService.findByName("DEALER");
		get().setPrimaryPricingType(type);
		Serviced.super.save();
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	public List<Customer> search(String text) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		String endpoint = text.isEmpty() ? "" : "/search?name=" + text;
		return customers = readOnlyService.module(getModule()).getList(endpoint);
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		customer = (Customer) t;
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

	public void setParentIfExists(Long id) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, DeactivatedException, RestException {
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

	public void setType(PartnerType value) {
		get().setType(value);
	}

	public void setVisitFrequency(VisitFrequency value) {
		get().setVisitFrequency(value);
	}

	public void setVisitSchedule(List<WeeklyVisit> items) {
		get().setVisitSchedule(items);
	}

	@Override
	public void updatePerValidity(Boolean isValid, String remarks) {
		if (tab.equals(CREDIT_TAB))
			approveCreditDetail(isValid, remarks);
		if (tab.equals(DISCOUNT_TAB))
			approveDiscount(isValid, remarks);
	}

	public void validatePhoneNo(String ph) throws InvalidException {
		if (!NumberUtils.isPhone(ph))
			throw new InvalidException(ph + " is an invalid phone number");
		get().setMobile(NumberUtils.persistPhone(ph));
	}

	private void approveCreditDetail(Boolean isValid, String remarks) {
		List<CreditDetail> list = approve(get().getCreditDetails(), isValid, remarks);
		get().setCreditDetails(list);
	}

	private void approveDiscount(Boolean isValid, String remarks) {
		List<CustomerDiscount> list = approve(get().getCustomerDiscounts(), isValid, remarks);
		get().setCustomerDiscounts(list);
	}

	private List<WeeklyVisit> blankSchedule() {
		List<WeeklyVisit> l = new ArrayList<>();
		for (int i = 1; i <= 5; i++)
			l.add(new WeeklyVisit(i, false, false, false, false, false, false, false));
		return l;
	}

	private CreditDetail createCreditLine(int term, int gracePeriod, BigDecimal creditLimit, LocalDate startDate) {
		CreditDetail c = new CreditDetail();
		c.setTermInDays(term);
		c.setGracePeriodInDays(gracePeriod);
		c.setCreditLimit(creditLimit);
		c.setStartDate(startDate);
		updateCreditDetails(c);
		return c;
	}

	private CustomerDiscount createCustomerDiscount(int level, BigDecimal percent, ItemFamily family,
			LocalDate startDate) {
		CustomerDiscount d = new CustomerDiscount();
		d.setLevel(level);
		d.setPercent(percent);
		d.setFamilyLimit(family);
		d.setStartDate(startDate);
		updateCustomerDiscounts(d);
		return d;
	}

	private Routing createRouteAssignment(Route route, LocalDate startDate) {
		Routing r = new Routing();
		r.setRoute(route);
		r.setStartDate(startDate);
		updateRouteHistory(r);
		return r;
	}

	private List<CreditDetail> creditDetails() {
		if (get().getCreditDetails() == null)
			get().setCreditDetails(new ArrayList<>());
		return get().getCreditDetails();
	}

	private List<CustomerDiscount> customerDiscounts() {
		if (get().getCustomerDiscounts() == null)
			get().setCustomerDiscounts(new ArrayList<>());
		return get().getCustomerDiscounts();
	}

	private String getExcelFileName() {
		return getExcelSheetName() + "." + toHypenatedYearMonthDay(now());
	}

	private String getExcelSheetName() {
		return "Active.Customers";
	}

	private Customer getParent() {
		return get().getParent();
	}

	private boolean isAVisitedChannel(Channel c) {
		String n = c.getName();
		return !listVisitedChannels().contains(n);
	}

	private List<Channel> listChannels() {
		try {
			return channelService.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private <T extends EntityDecisionNeeded<Long>> boolean noChanges(List<T> l) {
		return l == null ? true : !l.stream().anyMatch(d -> d.getIsValid() == null);
	}

	private List<Routing> routeHistory() {
		if (get().getRouteHistory() == null)
			setRouteHistory(emptyList());
		return get().getRouteHistory();
	}

	private void updateCreditDetails(CreditDetail credit) {
		List<CreditDetail> list = new ArrayList<>(get().getCreditDetails());
		list.add(credit);
		get().setCreditDetails(list);
	}

	private void updateCustomerDiscounts(CustomerDiscount customerDiscount) {
		List<CustomerDiscount> list = new ArrayList<>(get().getCustomerDiscounts());
		list.add(customerDiscount);
		get().setCustomerDiscounts(list);
	}

	private void updateRouteHistory(Routing routing) {
		List<Routing> list = new ArrayList<>(get().getRouteHistory());
		list.add(routing);
		setRouteHistory(list);
	}

	private void validateDateAndDiscountLevelAreUnique(List<CustomerDiscount> list, LocalDate startDate, int level)
			throws DuplicateException {
		boolean exists = list.stream().anyMatch(r -> r.getStartDate().equals(startDate) && level == r.getLevel());
		if (exists)
			throw new DuplicateException("Discount level " + level + " of start date " + toDateDisplay(startDate));
	}

	private void validateStartDate(List<? extends StartDated> list, LocalDate startDate)
			throws DateInThePastException, DuplicateException {
		validateDateIsNotInThePast(startDate);
		validateDateIsUnique(list, startDate);
	}

	private void validateStartDate(List<CustomerDiscount> list, LocalDate startDate, int level)
			throws DateInThePastException, DuplicateException {
		validateDateIsNotInThePast(startDate);
		validateDateAndDiscountLevelAreUnique(list, startDate, level);
	}

	private List<WeeklyVisit> visitSchedule() {
		List<WeeklyVisit> l = get().getVisitSchedule();
		return l.isEmpty() ? blankSchedule() : l;
	}
}
