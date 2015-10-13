package ph.txtdis.converter;

import java.time.ZonedDateTime;

import javafx.util.StringConverter;
import ph.txtdis.util.Temporal;

public class DateTimeConverter extends StringConverter<ZonedDateTime> {

	@Override
	public String toString(ZonedDateTime dateTime) {
		return Temporal.format(dateTime);
	}

	@Override
	public ZonedDateTime fromString(String text) {
		return Temporal.toZonedDateTime(text);
	}
}
