package ph.txtdis.fx;

import static ph.txtdis.util.NumberUtils.displayPhone;
import static ph.txtdis.util.NumberUtils.format2Place;
import static ph.txtdis.util.NumberUtils.format4Place;
import static ph.txtdis.util.NumberUtils.formatCurrency;
import static ph.txtdis.util.NumberUtils.formatDecimal;
import static ph.txtdis.util.NumberUtils.formatId;
import static ph.txtdis.util.NumberUtils.formatInt;
import static ph.txtdis.util.NumberUtils.formatPercent;
import static ph.txtdis.util.NumberUtils.isNegative;
import static ph.txtdis.util.TextUtils.toBoolSign;
import static ph.txtdis.util.TextUtils.toIdDisplay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;
import static ph.txtdis.util.DateTimeUtils.toTimeDisplay;
import static ph.txtdis.util.DateTimeUtils.toTimestampText;

import ph.txtdis.fx.control.StylableTextField;

public class Styled {
	private static final String ALIGN = " -fx-alignment:";

	private static final String COLOR = " -fx-t-fill:";

	private static final String UNDIMMED = " -fx-opacity: 1; ";

	private static final String CENTER = UNDIMMED + ALIGN + " center; ";

	private static final String RIGHT_ALIGN = UNDIMMED + ALIGN + " center-right; ";

	private static final String GREEN = COLOR + " green; ";

	private static final String RED = COLOR + " red; ";

	public static void for2Place(StylableTextField f, BigDecimal d) {
		f.setText(format2Place(d));
		setNumberStyle(f, d);
	}

	public static void for4Place(StylableTextField f, BigDecimal d) {
		f.setText(format4Place(d));
		setNumberStyle(f, d);
	}

	public static void forBoolean(StylableTextField f, Boolean b) {
		f.setText(toBoolSign(b));
		f.setStyle(CENTER + (b == null ? "" : b ? GREEN : RED));
	}

	public static void forCode(StylableTextField f, String c) {
		f.setText(toIdDisplay(c));
		f.setStyle(RIGHT_ALIGN);
	}

	public static void forCurrency(StylableTextField f, BigDecimal d) {
		f.setText(formatCurrency(d));
		setNumberStyle(f, d);
	}

	public static void forDate(StylableTextField f, LocalDate d) {
		f.setText(toDateDisplay(d));
		center(f);
	}

	public static <T> void forEnum(StylableTextField f, T t) {
		f.setText(t.toString());
		center(f);
	}

	public static void forIdNo(StylableTextField f, Long l) {
		f.setText(formatId(l));
		setNumberStyle(f, l);
	}

	public static void forInteger(StylableTextField f, Integer i) {
		f.setText(formatInt(i));
		setNumberStyle(f, i);
	}

	public static void forPercent(StylableTextField f, BigDecimal d) {
		f.setText(formatPercent(d));
		setNumberStyle(f, d);
	}

	public static void forPhone(StylableTextField f, String t) {
		f.setText(displayPhone(t));
		f.setStyle(RIGHT_ALIGN);
	}

	public static void forQuantity(StylableTextField f, BigDecimal d) {
		f.setText(formatDecimal(d));
		setNumberStyle(f, d);
	}

	public static <T> void forText(StylableTextField f, T t) {
		f.setText(t == null ? "" : t.toString());
		f.setStyle(UNDIMMED);
	}

	public static void forTime(StylableTextField f, LocalTime t) {
		f.setText(toTimeDisplay(t));
		f.setStyle(UNDIMMED);
	}

	public static void forTimestamp(StylableTextField f, ZonedDateTime t) {
		f.setText(toTimestampText(t));
		center(f);
	}

	private static void center(StylableTextField f) {
		f.setStyle(CENTER);
	}

	private static void setNumberStyle(StylableTextField f, BigDecimal d) {
		f.setStyle(RIGHT_ALIGN + (isNegative(d) ? RED : ""));
	}

	private static void setNumberStyle(StylableTextField f, Integer i) {
		f.setStyle(RIGHT_ALIGN + (isNegative(i) ? RED : ""));
	}

	private static void setNumberStyle(StylableTextField f, Long l) {
		f.setStyle(RIGHT_ALIGN + (isNegative(l) ? RED : ""));
	}
}
