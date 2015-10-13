package ph.txtdis.exception;

import ph.txtdis.dto.InvoiceBooklet;

public class InvoiceIdInBookletAlreadyIssuedException extends Exception {

	private static final long serialVersionUID = -1428885265318163309L;

	public InvoiceIdInBookletAlreadyIssuedException(String id, InvoiceBooklet booklet) {
		super("S/I No. " + id + " exists in issued\n" + booklet);
	}
}
