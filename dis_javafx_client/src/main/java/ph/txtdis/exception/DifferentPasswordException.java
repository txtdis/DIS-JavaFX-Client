package ph.txtdis.exception;

public class DifferentPasswordException extends Exception {

    private static final long serialVersionUID = -187373205883903642L;

    public DifferentPasswordException() {
        super("Passwords differ");
    }
}
