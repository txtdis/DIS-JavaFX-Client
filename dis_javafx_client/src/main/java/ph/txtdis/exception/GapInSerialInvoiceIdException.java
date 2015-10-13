package ph.txtdis.exception;

public class GapInSerialInvoiceIdException extends Exception {

	private static final long serialVersionUID = 5553837883932799884L;

	public GapInSerialInvoiceIdException(Long id) {
		super("S/I No. " + id + " must be used first");
	}
}
