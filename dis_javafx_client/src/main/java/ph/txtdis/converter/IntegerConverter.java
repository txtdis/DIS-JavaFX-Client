package ph.txtdis.converter;

import javafx.util.StringConverter;
import ph.txtdis.util.Numeric;

public class IntegerConverter extends StringConverter<Integer> {

	@Override
	public Integer fromString(String text) {
		return Numeric.parseInteger(text);
	}

	@Override
	public String toString(Integer number) {
		return Numeric.formatInt(number);
	}
}
