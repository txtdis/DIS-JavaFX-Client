package ph.txtdis.converter;

import javafx.util.StringConverter;
import ph.txtdis.util.NumberUtils;

public class IntegerConverter extends StringConverter<Integer> {

	@Override
	public Integer fromString(String text) {
		return NumberUtils.toInteger(text);
	}

	@Override
	public String toString(Integer number) {
		return NumberUtils.formatInt(number);
	}
}
