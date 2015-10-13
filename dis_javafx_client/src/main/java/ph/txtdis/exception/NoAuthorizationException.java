package ph.txtdis.exception;

import javax.naming.AuthenticationException;

public class NoAuthorizationException extends AuthenticationException {

    private static final long serialVersionUID = -3814006856179162000L;

    public NoAuthorizationException() {
        super("No/Insufficient authorization");
    }
}
