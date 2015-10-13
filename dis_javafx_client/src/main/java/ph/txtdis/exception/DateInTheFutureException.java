package ph.txtdis.exception;

public class DateInTheFutureException extends Exception {

    private static final long serialVersionUID = 1651755608620016409L;

    public DateInTheFutureException() {
        super("Date cannot be in the future");
    }
}
