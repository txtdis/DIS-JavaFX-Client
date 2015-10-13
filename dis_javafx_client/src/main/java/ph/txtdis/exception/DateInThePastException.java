package ph.txtdis.exception;

public class DateInThePastException extends Exception {

    private static final long serialVersionUID = 509462896371118219L;

    public DateInThePastException() {
        super("Date cannot be in the past");
    }
}
