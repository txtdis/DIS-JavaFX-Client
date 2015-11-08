package ph.txtdis.converter;

import javafx.util.StringConverter;
import ph.txtdis.util.NumberUtils;

public class PhoneConverter extends StringConverter<String> {

    @Override
    public String toString(String phone) {
        return NumberUtils.displayPhone(phone);
    }

    @Override
    public String fromString(String phone) {
        return NumberUtils.displayPhone(phone);
    }
}
