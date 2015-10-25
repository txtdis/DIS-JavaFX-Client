package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerReceivable;
import ph.txtdis.exception.BadCreditException;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.util.Numeric;

@Service("bookingService")
public class BookingService extends SoldService<Booking, Long> implements AlternateNamed, Reset {

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

	public List<Booking> listByPickDate(LocalDate d) throws Exception {
		return readOnlyService.module(getModule()).getList("/pick?date=" + d);
	}

	@Override
	public void reset() {
		set(new Booking());
	}

	@Override
	public void setOrderDateUponValidation(LocalDate d) throws Exception {
		if (d == null)
			return;
		if (d.isBefore(LocalDate.now()))
			throw new DateInThePastException();
		setOrderDateAfterReset(d);
	}

	public void updateUponCustomerIdValidation(Long id) throws Exception {
		Customer c = customerService.find(id.toString());
		verifyCustomerHasNoOverdues(c);
		setCustomerAfterReset(c);
	}

	private BigDecimal overdueValue(Customer c) throws Exception {
		customerReceivableService.listInvoicesByCustomerBetweenTwoDayCounts(c.getId().toString(), A_DAY_OVER,
				MAX_DAYS_OVER);
		List<CustomerReceivable> list = customerReceivableService.list();
		return list.stream().map(r -> r.getUnpaidValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	private void setCustomerAfterReset(Customer c) {
		LocalDate d = getOrderDate();
		setOrderDateAfterReset(d);
		get().setCustomer(c);
	}

	private void verifyCustomerHasNoOverdues(Customer c) throws Exception {
		BigDecimal o = overdueValue(c);
		if (Numeric.isPositive(o))
			throw new BadCreditException(c, o);
	}
}
