package ph.txtdis.exception;


public class InvalidBookletSizeException extends Exception {

    private static final long serialVersionUID = -6542912129473320508L;

    public InvalidBookletSizeException() {
        super("Booklets must have 50 leaves");
    }
}
