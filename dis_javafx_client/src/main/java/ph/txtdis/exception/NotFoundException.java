package ph.txtdis.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6046598298985805710L;

    public NotFoundException(String message) {
        super(message + "\ndoes not exist");
    }
}
