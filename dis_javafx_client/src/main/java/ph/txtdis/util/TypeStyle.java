package ph.txtdis.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;

import javafx.geometry.Pos;
import ph.txtdis.fx.DecimalInputValidator;
import ph.txtdis.fx.IntegerInputValidator;
import ph.txtdis.fx.PhoneInputValidator;
import ph.txtdis.fx.Styled;
import ph.txtdis.fx.ToUpperCaseConverter;
import ph.txtdis.fx.ToUpperOnlyConverter;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.type.Type;

public class TypeStyle {

	public static <T> void align(Type type, AppField<T> field) {
		switch (type) {
			case CURRENCY:
			case DECIMAL:
			case FOURPLACE:
			case ID:
			case INTEGER:
			case PERCENT:
			case PHONE:
				field.setAlignment(Pos.CENTER_RIGHT);
				break;
			default:
				field.setAlignment(Pos.CENTER_LEFT);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parse(Type type, String text) {
		switch (type) {
			case CURRENCY:
			case DECIMAL:
			case FOURPLACE:
			case PERCENT:
				return (T) Numeric.parseBigDecimal(text);
			case ID:
				return (T) Numeric.parseLong(text);
			case INTEGER:
				return (T) Numeric.parseInteger(text);
			case PHONE:
				return (T) Numeric.persistPhone(text);
			case TEXT:
			case ALPHA:
				return text == null ? null : (T) StringUtils.trim(text);
			default:
				return null;
		}
	}

	public static <T> void style(Type type, StylableTextField field, T value) {
		switch (type) {
			case CURRENCY:
				Styled.forCurrency(field, (BigDecimal) value);
				break;
			case DATE:
				Styled.forDate(field, (LocalDate) value);
				break;
			case DECIMAL:
				Styled.forDecimal(field, (BigDecimal) value);
				break;
			case ENUM:
				Styled.forEnum(field, value);
				break;
			case FOURPLACE:
				Styled.for4Place(field, (BigDecimal) value);
				break;
			case ID:
				Styled.forIdNo(field, (Long) value);
				break;
			case INTEGER:
				Styled.forInteger(field, (Integer) value);
				break;
			case PERCENT:
				Styled.forPercent(field, (BigDecimal) value);
				break;
			case PHONE:
				Styled.forPhone(field, (String) value);
				break;
			case TIMESTAMP:
				Styled.forTimestamp(field, (ZonedDateTime) value);
				break;
			default:
				Styled.forText(field, value);
		}
	}

	public static <T> void validate(Type type, AppField<T> field) {
		switch (type) {
			case CURRENCY:
			case DECIMAL:
			case FOURPLACE:
			case PERCENT:
				field.textProperty().addListener(new DecimalInputValidator(field));
				break;
			case ID:
			case INTEGER:
				field.textProperty().addListener(new IntegerInputValidator(field));
				break;
			case PHONE:
				field.setPromptText("0888 888 8888");
				field.textProperty().addListener(new PhoneInputValidator(field));
				break;
			case TEXT:
				field.textProperty().addListener(new ToUpperCaseConverter(field));
			case ALPHA:
				field.textProperty().addListener(new ToUpperOnlyConverter(field));
				break;
			default:
		}
	}

	public static int width(Type type) {
		switch (type) {
			case INTEGER:
			case ENUM:
			case FOURPLACE:
				return 80;
			case ID:
				return 90;
			case DATE:
			case DECIMAL:
				return 110;
			case TIMESTAMP:
			case PHONE:
				return 160;
			case TEXT:
				return 240;
			case CURRENCY:
			case OTHERS:
			case ALPHA:
			default:
				return 120;
		}
	}
}
