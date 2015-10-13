package ph.txtdis.exception;

public class UnissuedInvoiceIdException extends Exception {

	private static final long serialVersionUID = -6318336145443848077L;

	public UnissuedInvoiceIdException(String id) {
		super("S/I No. " + id + " is NOT part\nof any issued booklet");
	}
}
