package ph.txtdis.util;

import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static ph.txtdis.util.NumberUtils.persistPhone;
import static ph.txtdis.util.NumberUtils.toBigDecimal;
import static ph.txtdis.util.NumberUtils.toInteger;
import static ph.txtdis.util.NumberUtils.toLong;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.apache.commons.lang3.StringUtils.trim;

import static ph.txtdis.util.DateTimeUtils.toTime;

import ph.txtdis.fx.AlphabetOnlyValidator;
import ph.txtdis.fx.DecimalInputValidator;
import ph.txtdis.fx.IntegerInputValidator;
import ph.txtdis.fx.PhoneInputValidator;
import ph.txtdis.fx.Styled;
import ph.txtdis.fx.TextValidator;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.type.Type;

public class TypeStyle {

	public static <T> void align(Type type, AppField<T> field) {
		switch (type) {
			case CODE:
			case CURRENCY:
			case DECIMAL:
			case FOURPLACE:
			case ID:
			case INTEGER:
			case PERCENT:
			case PHONE:
				field.setAlignment(CENTER_RIGHT);
				break;
			default:
				field.setAlignment(CENTER_LEFT);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parse(Type type, String text) {
		switch (type) {
			case CURRENCY:
			case DECIMAL:
			case FOURPLACE:
			case PERCENT:
				return (T) toBigDecimal(text);
			case ID:
				return (T) toLong(text);
			case INTEGER:
				return (T) toInteger(text);
			case PHONE:
				return (T) persistPhone(text);
			case TIME:
				return (T) toTime(text);
			case ALPHA:
			case CODE:
			case TEXT:
				return text == null ? null : (T) trim(text);
			default:
				return null;
		}
	}

	public static <T> void style(Type type, StylableTextField field, T value) {
		if (type == null)
			return;
		switch (type) {
			case CODE:
				Styled.forCode(field, value);
				break;
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
			case TIME:
				Styled.forTime(field, (LocalTime) value);
				break;
			case TIMESTAMP:
				Styled.forTimestamp(field, (ZonedDateTime) value);
				break;
			case ALPHA:
			case TEXT:
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
			case CODE:
			case TEXT:
				field.textProperty().addListener(new TextValidator(field));
				break;
			case TIME:
				field.setPromptText("hh:mm");
				break;
			case ALPHA:
				field.textProperty().addListener(new AlphabetOnlyValidator(field));
				break;
			default:
		}
	}

	public static int width(Type type) {
		switch (type) {
			case ALPHA:
				return 60;
			case INTEGER:
			case ENUM:
			case FOURPLACE:
			case TIME:
				return 80;
			case ID:
			case DATE:
				return 90;
			case DECIMAL:
				return 110;
			case TIMESTAMP:
			case PHONE:
				return 160;
			case TEXT:
				return 240;
			case CODE:
			case CURRENCY:
			case OTHERS:
			default:
				return 120;
		}
	}
}
