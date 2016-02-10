package ph.txtdis.exception;

import static ph.txtdis.util.NumberUtils.formatCurrency;

import java.math.BigDecimal;

import ph.txtdis.dto.Customer;

public class ExceededCreditLimitException extends Exception {

	private static final long serialVersionUID = -6656257234900338909L;

	public ExceededCreditLimitException(Customer c, BigDecimal limit, BigDecimal excess) {
		super(c + "\nhas exceeded its limit of " //
				+ formatCurrency(limit) + " by " + formatCurrency(excess));
	}
}
