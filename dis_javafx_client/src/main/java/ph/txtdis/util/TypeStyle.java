package ph.txtdis.util;

import static javafx.geometry.Pos.CENTER_LEFT;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static ph.txtdis.fx.Styled.for2Place;
import static ph.txtdis.fx.Styled.for4Place;
import static ph.txtdis.fx.Styled.forBoolean;
import static ph.txtdis.fx.Styled.forCode;
import static ph.txtdis.fx.Styled.forCurrency;
import static ph.txtdis.fx.Styled.forDate;
import static ph.txtdis.fx.Styled.forEnum;
import static ph.txtdis.fx.Styled.forIdNo;
import static ph.txtdis.fx.Styled.forInteger;
import static ph.txtdis.fx.Styled.forPercent;
import static ph.txtdis.fx.Styled.forPhone;
import static ph.txtdis.fx.Styled.forQuantity;
import static ph.txtdis.fx.Styled.forText;
import static ph.txtdis.fx.Styled.forTime;
import static ph.txtdis.fx.Styled.forTimestamp;
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
import ph.txtdis.fx.TextValidator;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.type.Type;

public class TypeStyle {

	public static <T> void align(Type type, AppField<T> field) {
		switch (type) {
			case CODE:
			case CURRENCY:
			case FOURPLACE:
			case ID:
			case INTEGER:
			case PERCENT:
			case PHONE:
			case QUANTITY:
			case TWOPLACE:
				field.setAlignment(CENTER_RIGHT);
				break;
			default:
				field.setAlignment(CENTER_LEFT);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parse(Type type, String text) {
		switch (type) {
			case ALPHA:
			case CODE:
			case TEXT:
			case PHONE:
				return text == null ? null : (T) trim(text);
			case CURRENCY:
			case FOURPLACE:
			case PERCENT:
			case QUANTITY:
			case TWOPLACE:
				return (T) toBigDecimal(text);
			case ID:
				return (T) toLong(text);
			case INTEGER:
				return (T) toInteger(text);
			case TIME:
				return (T) toTime(text);
			default:
				return null;
		}
	}

	public static <T> void style(Type type, StylableTextField field, T value) {
		if (type == null)
			return;
		switch (type) {
			case BOOLEAN:
				forBoolean(field, (Boolean) value);
				break;
			case CODE:
				forCode(field, (String) value);
				break;
			case CURRENCY:
				forCurrency(field, (BigDecimal) value);
				break;
			case DATE:
				forDate(field, (LocalDate) value);
				break;
			case ENUM:
				forEnum(field, value);
				break;
			case FOURPLACE:
				for4Place(field, (BigDecimal) value);
				break;
			case ID:
				forIdNo(field, (Long) value);
				break;
			case INTEGER:
				forInteger(field, (Integer) value);
				break;
			case PERCENT:
				forPercent(field, (BigDecimal) value);
				break;
			case QUANTITY:
				forQuantity(field, (BigDecimal) value);
				break;
			case PHONE:
				forPhone(field, (String) value);
				break;
			case TIME:
				forTime(field, (LocalTime) value);
				break;
			case TIMESTAMP:
				forTimestamp(field, (ZonedDateTime) value);
				break;
			case TWOPLACE:
				for2Place(field, (BigDecimal) value);
				break;
			case ALPHA:
			case TEXT:
			default:
				forText(field, value);
		}
	}

	public static <T> void validate(Type type, AppField<T> field) {
		switch (type) {
			case ALPHA:
				field.textProperty().addListener(new AlphabetOnlyValidator(field));
				break;
			case CODE:
			case TEXT:
				field.textProperty().addListener(new TextValidator(field));
				break;
			case CURRENCY:
			case FOURPLACE:
			case PERCENT:
			case QUANTITY:
			case TWOPLACE:
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
			case TIME:
				field.setPromptText("hh:mm");
				break;
			default:
		}
	}

	public static int width(Type type) {
		switch (type) {
			case ALPHA:
			case BOOLEAN:
			case CHECKBOX:
				return 60;
			case ENUM:
			case FOURPLACE:
			case INTEGER:
			case TIME:
			case ID:
			case PERCENT:
			case TWOPLACE:
				return 90;
			case DATE:
			case QUANTITY:
				return 110;
			case PHONE:
			case TIMESTAMP:
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
