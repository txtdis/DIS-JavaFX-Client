package ph.txtdis.exception;

import java.math.BigDecimal;

import ph.txtdis.dto.Customer;
import ph.txtdis.util.Numeric;

public class ExceededCreditLimitException extends Exception {

    private static final long serialVersionUID = -1980107032010503069L;

    public ExceededCreditLimitException(Customer customer,
            BigDecimal creditLimit) {
        super(customer + " has exceeded its\n" + Numeric.formatCurrency(
                creditLimit) + " credit limit");
    }

    public ExceededCreditLimitException(BigDecimal creditLimit) {
        super("Credit limit of " + Numeric.formatCurrency(creditLimit)
                + " exceeded");
    }
}
