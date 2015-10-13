package ph.txtdis.exception;

import java.math.BigDecimal;

import ph.txtdis.dto.Customer;
import ph.txtdis.util.Numeric;

public class BadCreditException extends Exception {

    private static final long serialVersionUID = -6656257234900338909L;

    public BadCreditException(Customer customer, BigDecimal overdue) {
        super(customer + " has " + Numeric.formatCurrency(overdue)
                + " overdue");
    }
}
