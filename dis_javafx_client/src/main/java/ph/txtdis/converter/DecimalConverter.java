package ph.txtdis.converter;

import java.math.BigDecimal;

import javafx.util.StringConverter;
import ph.txtdis.util.Numeric;

public class DecimalConverter extends StringConverter<BigDecimal> {

    @Override
    public String toString(BigDecimal number) {
        return Numeric.formatDecimal(number);
    }

    @Override
    public BigDecimal fromString(String text) {
        return Numeric.parseBigDecimal(text);
    }
}
