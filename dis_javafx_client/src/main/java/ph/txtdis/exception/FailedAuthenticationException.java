package ph.txtdis.exception;

public class FailedAuthenticationException extends Exception {

    private static final long serialVersionUID = -2665228278605327804L;

    public FailedAuthenticationException() {
        super("Incorrect username and/or password");
    }
}
