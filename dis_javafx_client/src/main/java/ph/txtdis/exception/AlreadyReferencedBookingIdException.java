package ph.txtdis.exception;

import ph.txtdis.dto.AbstractBookedOrder;

public class AlreadyReferencedBookingIdException extends Exception {

	private static final long serialVersionUID = -1428885265318163309L;

	public AlreadyReferencedBookingIdException(Long bookingId, AbstractBookedOrder<?> booked) {
		super("S/O No. " + bookingId + "\nis referenced in\n" + booked);
	}
}
