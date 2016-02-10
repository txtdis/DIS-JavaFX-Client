package ph.txtdis.exception;

import static ph.txtdis.util.NumberUtils.formatCurrency;

import java.math.BigDecimal;

import ph.txtdis.dto.Customer;

public class BadCreditException extends Exception {

	private static final long serialVersionUID = -6656257234900338909L;

	public BadCreditException(Customer c, BigDecimal overdue) {
		super(c + "\nhas " + formatCurrency(overdue) + " overdue");
	}
}
