package ph.txtdis.converter;

import java.time.LocalDate;

import javafx.util.StringConverter;
import ph.txtdis.util.Temporal;

public class DateDisplayConverter extends StringConverter<LocalDate> {

	@Override
	public String toString(LocalDate date) {
		return Temporal.format(date);
	}

	@Override
	public LocalDate fromString(String text) {
		return Temporal.toLocalDate(text);
	}
}
