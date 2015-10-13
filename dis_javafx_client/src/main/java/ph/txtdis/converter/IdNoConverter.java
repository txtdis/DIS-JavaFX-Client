package ph.txtdis.converter;

import javafx.util.StringConverter;
import ph.txtdis.util.Numeric;

public class IdNoConverter extends StringConverter<Long> {

    @Override
    public String toString(Long number) {
        return Numeric.formatId(number);
    }

    @Override
    public Long fromString(String text) {
        return Numeric.parseLong(text);
    }
}
