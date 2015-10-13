package ph.txtdis.converter;

import javafx.util.StringConverter;
import ph.txtdis.util.Numeric;

public class PhoneConverter extends StringConverter<String> {

    @Override
    public String toString(String phone) {
        return Numeric.displayPhone(phone);
    }

    @Override
    public String fromString(String phone) {
        return Numeric.displayPhone(phone);
    }
}
