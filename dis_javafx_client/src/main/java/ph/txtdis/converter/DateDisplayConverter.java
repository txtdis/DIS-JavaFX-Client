package ph.txtdis.converter;

import java.time.LocalDate;

import javafx.util.StringConverter;
import ph.txtdis.util.DateTimeUtils;

public class DateDisplayConverter extends StringConverter<LocalDate> {

	@Override
	public String toString(LocalDate date) {
		return DateTimeUtils.toDateDisplay(date);
	}

	@Override
	public LocalDate fromString(String text) {
		return DateTimeUtils.toDate(text);
	}
}
