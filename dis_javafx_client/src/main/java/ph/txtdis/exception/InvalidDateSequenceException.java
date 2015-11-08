package ph.txtdis.exception;

import java.time.LocalDate;

import ph.txtdis.util.DateTimeUtils;

public class InvalidDateSequenceException extends Exception {

	private static final long serialVersionUID = 4778948532721774491L;

	public InvalidDateSequenceException(String module1, LocalDate date1, String module2, LocalDate date2) {
		super(module1 + " Date of " + DateTimeUtils.toDateDisplay(date1) + "\nCANNOT be before\n" + module2 + " Date of "
				+ DateTimeUtils.toDateDisplay(date2));
	}
}
