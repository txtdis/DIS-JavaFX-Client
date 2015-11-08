package ph.txtdis.converter;

import java.time.ZonedDateTime;

import javafx.util.StringConverter;
import ph.txtdis.util.DateTimeUtils;

public class DateTimeConverter extends StringConverter<ZonedDateTime> {

	@Override
	public String toString(ZonedDateTime dateTime) {
		return DateTimeUtils.toTimestampText(dateTime);
	}

	@Override
	public ZonedDateTime fromString(String text) {
		return DateTimeUtils.toZonedDateTime(text);
	}
}
