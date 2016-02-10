package ph.txtdis.exception;

public class NotAllowedOffSiteTransactionException extends Exception {

	private static final long serialVersionUID = -6918884338346098939L;

	public NotAllowedOffSiteTransactionException() {
		super("Only allowed if on site");
	}
}
