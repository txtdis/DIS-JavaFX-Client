package ph.txtdis.service;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static ph.txtdis.type.PaymentType.CASH;
import static ph.txtdis.type.PaymentType.CHECK;
import static ph.txtdis.type.PaymentType.values;
import static ph.txtdis.type.UserType.CASHIER;
import static ph.txtdis.type.UserType.COLLECTOR;
import static ph.txtdis.type.UserType.MAIN_CASHIER;
import static ph.txtdis.type.UserType.MANAGER;
import static ph.txtdis.util.NumberUtils.isPositive;
import static ph.txtdis.util.SpringUtil.isUser;
import static ph.txtdis.util.SpringUtil.username;
import static ph.txtdis.util.TextUtils.blankIfNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toHypenatedYearMonthDay;

import ph.txtdis.dto.Billable;
import ph.txtdis.dto.CreationTracked;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Payment;
import ph.txtdis.dto.PaymentDetail;
import ph.txtdis.dto.PaymentHistory;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.PaymentType;
import ph.txtdis.util.DateTimeUtils;

@Service("remittanceService")
public class RemittanceService implements Detailed, NeededDecisionDisplayed, Reset, Serviced<Long>,
		Spreadsheet<PaymentHistory>, SpunById<Long>, CreationTracked
{

	private class CashCollectionHasBeenReceivedFromCollectorException extends Exception {

		private static final long serialVersionUID = -680021070658923416L;

		public CashCollectionHasBeenReceivedFromCollectorException(String collector, LocalDate d) {
			super("Cash collection has been received\nfrom " + collector + " on " + DateTimeUtils.toDateDisplay(d));
		}
	}

	private class DuplicateCheckException extends Exception {

		private static final long serialVersionUID = -4008903600748202903L;

		public DuplicateCheckException(Customer bank, Long checkId, Long collectionId) {
			super(bank + " Check No. " + checkId + "\nhas been used in\n" + "Collections Record No. " + collectionId);
		}
	}

	private class NotTodayOrYesterdayCashPaymentException extends Exception {

		private static final long serialVersionUID = -4430663872809922339L;

		public NotTodayOrYesterdayCashPaymentException() {
			super("Cash Payments must be dated today or yesterday.");
		}
	}

	private static final String COLLECTION = "Record of collection\n";

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ReadOnlyService<Payment> readOnlyService;

	@Autowired
	private ReadOnlyService<PaymentHistory> readOnlyPaymentHistoryService;

	@Autowired
	private SavingService<Payment> savingService;

	@Autowired
	private SavingService<List<Payment>> listSavingService;

	@Autowired
	private SpunService<Payment, Long> spunService;

	@Autowired
	private UserService userService;

	@Autowired
	private ExcelWriter excel;

	private BigDecimal remaining;

	private List<String> collectors;

	private List<Customer> banks;

	private Payment payment;

	public RemittanceService() {
		reset();
	}

	@Override
	public boolean canApprove() {
		if (!userIsAMainCashier())
			return false;
		if (get().getDepositedOn() == null)
			return false;
		return paidCash() ? true : checkCleared();
	}

	public PaymentDetail createDetail(Billable b, BigDecimal payment, BigDecimal remaining) {
		this.remaining = remaining;
		PaymentDetail d = new PaymentDetail();
		d.setId(b.getId());
		d.setOrderNo(b.getOrderNo());
		d.setCustomerName(b.getCustomerName());
		d.setDueDate(b.getDueDate());
		d.setTotalDueValue(b.getTotalValue());
		d.setPaymentValue(payment);
		return addDetail(d);
	}

	public List<Payment> findByBilling(Billable b) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getList("/find?billing=" + b.getId());
	}

	public boolean foundThisBillableOnThisPaymentList(Billable i) {
		try {
			return getDetails().stream().anyMatch(d -> d.getId().equals(i.getId()));
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Payment get() {
		if (payment == null)
			reset();
		return payment;
	}

	@Override
	public String getAlternateName() {
		return getHeaderText() + " Record";
	}

	public List<Customer> getBanks() {
		try {
			return customerService.getBanks();
		} catch (Exception e) {
			return null;
		}
	}

	public List<String> getCollectorNames() {
		if (collectors == null)
			collectors = getCollectors();
		return getId() == null ? collectors : asList(get().getCollector());
	}

	public List<String> getCollectors() {
		return userService.listNamesByRole(COLLECTOR);
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
		return get().getAuditedBy();
	}

	@Override
	public ZonedDateTime getDecidedOn() {
		return get().getAuditedOn();
	}

	public List<Customer> getDraweeBank() {
		if (banks == null)
			banks = getBanks();
		return getId() == null ? banks : asList(get().getDraweeBank());
	}

	@Override
	public String getHeaderText() {
		return "Collection";
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	@Override
	public Boolean getIsValid() {
		return get().getIsValid();
	}

	@Override
	public String getModule() {
		return "remittance";
	}

	@Override
	public String getModuleId() {
		return getHeaderText() + " ID ";
	}

	@Override
	public String getOpenDialogHeading() {
		return "Open a Collection Record";
	}

	public PaymentType[] getPaymentTypes() {
		if (isNew())
			return values();
		return new PaymentType[] { get().getCheckId() == null ? CASH : CHECK };
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<PaymentHistory> getReadOnlyService() {
		return readOnlyPaymentHistoryService;
	}

	public BigDecimal getRemaining() {
		return remaining;
	}

	@Override
	public String getRemarks() {
		return get().getRemarks();
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<Payment> getSavingService() {
		return savingService;
	}

	@Override
	public Long getSpunId() {
		return isNew() ? null : getId();
	}

	@Override
	public String getSpunModule() {
		return getModule();
	}

	@Override
	public SpunService<Payment, Long> getSpunService() {
		return spunService;
	}

	@Override
	public String getSubhead() {
		return null;
	}

	@Override
	public String getTitleText() {
		return getHeaderText() + " History";
	}

	@Override
	public List<BigDecimal> getTotals() {
		return null;
	}

	public Payment getUndepositedPayment(PaymentType t, String seller, LocalDate d) throws NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule())
				.getOne("/undeposited?payType=" + t + "&seller=" + seller + "&upTo=" + d);
	}

	@Override
	public boolean isAppendable() {
		return isPositive(remaining);
	}

	@Override
	public List<PaymentHistory> list() {
		return listPayments().stream().map(p -> toHistory(p)).collect(toList());
	}

	public void open(Customer bank, Long checkId) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, RestException {
		Payment p = find(bank, checkId);
		if (p == null)
			throw new NotFoundException(COLLECTION + "with " + bank + " check no. " + checkId);
		set(p);
	}

	@Override
	public void reset() {
		set(new Payment());
		remaining = null;
		collectors = null;
		banks = null;
	}

	public void resetInputDataRelatedToPayment() {
		if (isNew()) {
			get().setCheckId(null);
			get().setDraweeBank(null);
			get().setDetails(null);
		}
	}

	@Override
	public void save() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		set(savingService.module(getModule()).save(get()));
		if (get() != null)
			throw new SuccessfulSaveInfo(get());
	}

	public void save(List<Payment> l) throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		listSavingService.module(getModule()).save(l);
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	@Override
	public void saveDecision() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		save();
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		payment = (Payment) t;
	}

	public void setCheckId(Long id) {
		if (isNew())
			get().setCheckId(id);
	}

	public void setCollector(String s) {
		if (isNew())
			get().setCollector(s);
	}

	public void setDraweeBank(Customer c) {
		if (isNew())
			get().setDraweeBank(c);
	}

	public void setPayment(BigDecimal p) {
		get().setValue(p);
		remaining = p;
	}

	@Override
	public void updatePerValidity(Boolean isValid, String remarks) {
		get().setIsValid(isValid);
		get().setRemarks(blankIfNull(get().getRemarks()) + blankIfNull(remarks));
		get().setAuditedBy(isValid == null ? null : username());
		get().setAuditedOn(isValid == null ? null : now());
	}

	public boolean userAllowedToMakeCashDeposits() {
		return userIsACashier();
	}

	public boolean userAllowedToMakeCheckDeposits() {
		return userIsAMainCashier();
	}

	public boolean userAllowedToPostCollectionData() {
		return userIsACashier();
	}

	public boolean userAllowedToReceiveFundTransfer() {
		return userIsAMainCashier();
	}

	public void validateBankCheckBeforeSetting(Customer bank)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			DuplicateCheckException, RestException {
		Long checkId = get().getCheckId();
		Payment p = find(bank, checkId);
		if (p != null) {
			setCheckId(null);
			throw new DuplicateCheckException(bank, checkId, p.getId());
		}
		setDraweeBank(bank);
	}

	public void validateCashCollection() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, CashCollectionHasBeenReceivedFromCollectorException,
			RestException, NotTodayOrYesterdayCashPaymentException {
		LocalDate d = get().getPaymentDate();
		if (!isNew() || d == null)
			return;
		validateCashPaymentDateIsYesterdayOrToday(d);
		validateNoCashPaymentsReceivedFromCollector(d);
	}

	public void validateOrderDateBeforeSetting(LocalDate d) {
		if (d == null)
			return;
		get().setPaymentDate(d);
	}

	private PaymentDetail addDetail(PaymentDetail d) {
		List<PaymentDetail> l = getDetails();
		l.add(d);
		get().setDetails(l);
		return d;
	}

	private boolean checkCleared() {
		return LocalDate.now().isAfter(get().getPaymentDate().plusDays(3L));
	}

	private Payment find(Customer bank, Long checkId) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/check?bank=" + bank.getId() + "&id=" + checkId);
	}

	private List<PaymentDetail> getDetails() {
		List<PaymentDetail> l = get().getDetails();
		return l == null ? new ArrayList<>() : new ArrayList<>(l);
	}

	private String getExcelFileName() {
		return getTitleText().replace(" ", ".") + "." + getExcelSheetName();
	}

	private String getExcelSheetName() {
		return toHypenatedYearMonthDay(LocalDate.now().minusDays(15L)) + ".toDate";
	}

	private List<Payment> listPayments() {
		try {
			return readOnlyService.module(getModule()).getList();
		} catch (Exception e) {
			e.printStackTrace();
			return emptyList();
		}
	}

	private boolean paidCash() {
		return get().getCheckId() == null;
	}

	private PaymentHistory toHistory(Payment p) {
		PaymentHistory h = new PaymentHistory();
		h.setId(p.getId());
		h.setPaymentDate(p.getPaymentDate());
		h.setDepositBool(p.getDepositedOn() != null);
		h.setValid(p.getIsValid());
		h.setValue(p.getValue());
		return h;
	}

	private boolean userIsACashier() {
		return userIsAMainCashier() || isUser(CASHIER);
	}

	private boolean userIsAMainCashier() {
		return isUser(MAIN_CASHIER) || isUser(MANAGER);
	}

	private void validateCashPaymentDateIsYesterdayOrToday(LocalDate d) throws NotTodayOrYesterdayCashPaymentException {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1L);
		if (yesterday.getDayOfWeek() == SUNDAY)
			yesterday = today.minusDays(1L);
		if (d.isBefore(yesterday) || d.isAfter(today))
			throw new NotTodayOrYesterdayCashPaymentException();
	}

	private void validateNoCashPaymentsReceivedFromCollector(LocalDate d)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			RestException, CashCollectionHasBeenReceivedFromCollectorException {
		String c = get().getCollector();
		Payment p = readOnlyService.module(getModule()).getOne("/collector?name=" + c + "&date=" + d);
		if (p != null)
			throw new CashCollectionHasBeenReceivedFromCollectorException(c, d);
	}
}
