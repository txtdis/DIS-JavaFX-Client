package ph.txtdis.exception;

public class NotPickedBookingIdException extends Exception {

	private static final long serialVersionUID = 7504371666771238054L;

	public NotPickedBookingIdException(Long id) {
		super("S/O No. " + id + " has not been picked");
	}
}
