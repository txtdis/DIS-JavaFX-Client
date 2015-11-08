package ph.txtdis.service;

import static java.time.ZonedDateTime.now;
import static ph.txtdis.type.UserType.DRIVER;
import static ph.txtdis.type.UserType.SELLER;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.Spring.username;

import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Payment;
import ph.txtdis.dto.Tracked;
import ph.txtdis.dto.User;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.NotFoundException;

@Service("remittanceService")
public class RemittanceService implements Audited, Reset, Serviced<Payment, Long>, SpunById<Long>, Tracked {

	private static final String COLLECTION = "Record of collection\n";

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ReadOnlyService<Payment> readOnlyService;

	@Autowired
	private SavingService<Payment> savingService;

	@Autowired
	private SpunService<Payment, Long> spunService;

	@Autowired
	private UserService userService;

	private Payment remittance;

	private List<String> collectors;

	public RemittanceService() {
		reset();
	}

	@Override
	public Payment get() {
		if (remittance == null)
			reset();
		return remittance;
	}

	@Override
	public String getAlternateName() {
		return "Collection Record";
	}

	@Override
	public String getAuditedBy() {
		return get().getAuditedBy();
	}

	@Override
	public ZonedDateTime getAuditedOn() {
		return get().getAuditedOn();
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
		return getId() == null ? collectors : Arrays.asList(get().getCollector());
	}

	public List<String> getCollectors() {
		try {
			List<User> l = userService.listNamesByRole(DRIVER, SELLER);
			return l.stream().map(u -> u.getUsername()).collect(Collectors.toList());
		} catch (Exception e) {
			return null;
		}
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
		return getHeaderText() + " ID " + getId();
	}

	@Override
	public String getOpenDialogHeading() {
		return "Open a Collection Record";
	}

	@Override
	public ReadOnlyService<Payment> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public String getRemarks() {
		return get().getRemarks();
	}

	@Override
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

	public void open(Customer bank, Long checkId) throws Exception {
		set(find(bank, checkId));
	}

	@Override
	public void reset() {
		set(new Payment());
	}

	@Override
	public void set(Keyed<Long> r) {
		remittance = (Payment) r;
	}

	@Override
	public void setIsValid(Boolean isValid) {
		get().setIsValid(isValid);
	}

	public void setOrderDateUponValidation(LocalDate date) throws Exception {
		if (date == null)
			return;
		if (date.isAfter(LocalDate.now()))
			throw new DateInTheFutureException();
	}

	@Override
	public void setRemarks(String s) {
		get().setRemarks(s);
	}

	@Override
	public void updatePerValidity(Boolean isValid) {
		if (!isValid)
			negateBillablePaymentsMade();
		updateAuditData(isValid);
	}

	private Payment find(Customer bank, Long checkId) throws Exception {
		Payment p = getReadOnlyService().module(getModule()).getOne("/check?bank=" + bank.getId() + "&id=" + checkId);
		if (p == null)
			throw new NotFoundException(COLLECTION + "with " + bank + " check no. " + checkId);
		return p;
	}

	private void negateBillablePaymentsMade() {
		get().getDetails().forEach(d -> d.setPaymentValue(d.getPaymentValue().negate()));
	}

	private void updateAuditData(Boolean isValid) {
		get().setIsValid(isValid);
		get().setAuditedBy(username());
		get().setAuditedOn(now());
	}
}
