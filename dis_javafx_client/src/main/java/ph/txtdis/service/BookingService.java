package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Billable;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerReceivable;
import ph.txtdis.exception.BadCreditException;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.util.NumberUtils;

@Service("bookingService")
public class BookingService extends SoldService<Billable, Long> {

	private static final String A_DAY_OVER = "1";

	private static final String MAX_DAYS_OVER = String.valueOf(Integer.MAX_VALUE);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerReceivableService customerReceivableService;

	public BookingService() {
		reset();
	}

	@Override
	public String getAlternateName() {
		return "S/O";
	}

	@Override
	public String getModule() {
		return "booking";
	}

	@Override
	public String getRemarks() {
		return null;
	}

	public List<Billable> listByPickDate(LocalDate d) throws Exception {
		return getReadOnlyService().module(getModule()).getList("/pick?date=" + d);
	}

	@Override
	public void reset() {
		super.reset();
		set(new Billable());
	}

	@Override
	public void setOrderDateUponValidation(LocalDate d) throws Exception {
		if (d == null)
			return;
		if (d.isBefore(LocalDate.now()))
			throw new DateInThePastException();
		setOrderDateAfterReset(d);
	}

	@Override
	public void setRemarks(String s) {
		get().setRemarks(s);
	}

	@Override
	public void updatePerValidity(Boolean b) {
		// TODO Auto-generated method stub
	}

	public void updateUponCustomerIdValidation(Long id) throws Exception {
		Customer c = customerService.find(id.toString());
		verifyCustomerHasNoOverdues(c);
		setCustomerAndDatesAfterReset(c);
	}

	private LocalDate dueDate() {
		try {
			int term = getCredit().getTermInDays();
			return getOrderDate().plusDays(term);
		} catch (Exception e) {
			return null;
		}
	}

	private BigDecimal overdueValue(Customer c) throws Exception {
		customerReceivableService.listInvoicesByCustomerBetweenTwoDayCounts(c.getId().toString(), A_DAY_OVER,
				MAX_DAYS_OVER);
		List<CustomerReceivable> list = customerReceivableService.list();
		return list.stream().map(r -> r.getUnpaidValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	private void setCustomerAndDatesAfterReset(Customer c) {
		LocalDate d = getOrderDate();
		setOrderDateAfterReset(d);
		setCustomer(c);
		get().setDueDate(dueDate());
	}

	private void verifyCustomerHasNoOverdues(Customer c) throws Exception {
		BigDecimal o = overdueValue(c);
		if (NumberUtils.isPositive(o))
			throw new BadCreditException(c, o);
	}
}
