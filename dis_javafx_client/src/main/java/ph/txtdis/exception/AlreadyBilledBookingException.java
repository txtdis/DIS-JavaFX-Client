package ph.txtdis.exception;

public class AlreadyBilledBookingException extends Exception {

	private static final long serialVersionUID = -8123802272614410524L;

	public AlreadyBilledBookingException(Long bookingId, String orderNo) {
		super("S/O No. " + bookingId + "\nhas already been billed as\n" + "S/I(D/R) No." + orderNo);
	}

}
