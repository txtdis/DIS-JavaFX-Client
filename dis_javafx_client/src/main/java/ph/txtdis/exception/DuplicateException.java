package ph.txtdis.exception;

public class DuplicateException extends Exception {

    private static final long serialVersionUID = 3516715385558159044L;

    public DuplicateException(String message) {
        super(message + "\nalready exists");
    }
}
