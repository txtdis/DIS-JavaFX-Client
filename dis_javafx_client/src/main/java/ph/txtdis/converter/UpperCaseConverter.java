package ph.txtdis.converter;

import javafx.util.StringConverter;

public class UpperCaseConverter extends StringConverter<String> {

    @Override
    public String toString(String text) {
        return text == null ? null : text.toUpperCase();
    }

    @Override
    public String fromString(String text) {
        return text == null ? null : text.toUpperCase();
    }
}
