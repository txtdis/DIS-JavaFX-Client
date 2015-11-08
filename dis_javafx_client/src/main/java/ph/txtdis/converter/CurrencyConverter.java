package ph.txtdis.converter;

import java.math.BigDecimal;

import javafx.util.StringConverter;
import ph.txtdis.util.NumberUtils;

public class CurrencyConverter extends StringConverter<BigDecimal> {

    @Override
    public String toString(BigDecimal number) {
        return NumberUtils.formatCurrency(number);
    }

    @Override
    public BigDecimal fromString(String text) {
        return NumberUtils.toBigDecimal(text);
    }
}
