package ph.txtdis.fx;

import static ph.txtdis.util.NumberUtils.format4Place;
import static ph.txtdis.util.NumberUtils.formatCurrency;
import static ph.txtdis.util.NumberUtils.formatId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;
import static ph.txtdis.util.DateTimeUtils.toTimeDisplay;

import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.util.DateTimeUtils;
import ph.txtdis.util.NumberUtils;

public class Styled {
	private static final String UNDIMMED = "-fx-opacity: 1; ";

	private static final String RIGHT_ALIGN = UNDIMMED + " -fx-alignment: center-right; ";

	private static final String RED = " -fx-text-fill: red; ";

	public static void for4Place(StylableTextField field, BigDecimal number) {
		field.setText(format4Place(number));
		setNumberStyle(field, number);
	}

	public static <T> void forCode(StylableTextField field, T object) {
		field.setText(object == null ? "" : object.toString());
		field.setStyle(RIGHT_ALIGN);
	}

	public static void forCurrency(StylableTextField field, BigDecimal number) {
		field.setText(formatCurrency(number));
		setNumberStyle(field, number);
	}

	public static void forDate(StylableTextField field, LocalDate date) {
		field.setText(toDateDisplay(date));
		field.setStyle(UNDIMMED);
	}

	public static void forDecimal(StylableTextField field, BigDecimal number) {
		field.setText(NumberUtils.formatDecimal(number));
		setNumberStyle(field, number);
	}

	public static <T> void forEnum(StylableTextField field, T object) {
		field.setText(object.toString());
		center(field);
	}

	public static void forIdNo(StylableTextField field, Long number) {
		field.setText(formatId(number));
		setNumberStyle(field, number);
	}

	public static void forInteger(StylableTextField field, Integer number) {
		field.setText(NumberUtils.formatInt(number));
		setNumberStyle(field, number);
	}

	public static void forPercent(StylableTextField field, BigDecimal number) {
		field.setText(NumberUtils.formatPercent(number));
		setNumberStyle(field, number);
	}

	public static void forPhone(StylableTextField field, String text) {
		field.setText(NumberUtils.displayPhone(text));
		field.setStyle(RIGHT_ALIGN);
	}

	public static <T> void forText(StylableTextField field, T object) {
		field.setText(object == null ? "" : object.toString());
		field.setStyle(UNDIMMED);
	}

	public static void forTime(StylableTextField field, LocalTime t) {
		field.setText(toTimeDisplay(t));
		field.setStyle(UNDIMMED);
	}

	public static void forTimestamp(StylableTextField field, ZonedDateTime date) {
		field.setText(DateTimeUtils.toTimestampText(date));
		center(field);
	}

	private static void center(StylableTextField field) {
		field.setStyle(UNDIMMED + " -fx-alignment: center; ");
	}

	private static void setNumberStyle(StylableTextField field, BigDecimal number) {
		field.setStyle(RIGHT_ALIGN + (NumberUtils.isNegative(number) ? RED : ""));
	}

	private static void setNumberStyle(StylableTextField field, Integer number) {
		field.setStyle(RIGHT_ALIGN + (NumberUtils.isNegative(number) ? RED : ""));
	}

	private static void setNumberStyle(StylableTextField field, Long number) {
		field.setStyle(RIGHT_ALIGN + (NumberUtils.isNegative(number) ? RED : ""));
	}
}
