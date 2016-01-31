package ph.txtdis.exception;

public class UnbilledPickedSalesOrderException extends Exception {

	private static final long serialVersionUID = 4671867557387899786L;

	public UnbilledPickedSalesOrderException(Long id) {
		super("All picked bookings prior to cut-off\n"//
				+ "must be invoiced to continue;\n"//
				+ "S/O No. " + id + " is still not");
	}
}
