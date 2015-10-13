package ph.txtdis.exception;

import java.math.BigDecimal;

public class QtyExceedsPerCaseException extends Exception {

    private static final long serialVersionUID = 3833359335990046924L;

    public QtyExceedsPerCaseException(BigDecimal qty) {
        super("Quantity in PCs must be less than\nper CS of " + qty);
    }
}
