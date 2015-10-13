package ph.txtdis.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class Numeric {

	public final static DecimalFormat NO_COMMA_DECIMAL = new DecimalFormat("0.00;(0.00)");
	public final static DecimalFormat TWO_PLACE_DECIMAL = new DecimalFormat("#,##0.00;(#,##0.00)");
	public final static DecimalFormat FOUR_PLACE_DECIMAL = new DecimalFormat("#,##0.0000;(#,##0.0000)");
	public final static DecimalFormat INTEGER = new DecimalFormat("#,##0;(#,##0)");
	public final static DecimalFormat NO_COMMA_INTEGER = new DecimalFormat("0;(0)");

	public final static BigDecimal HUNDRED = new BigDecimal("100");

	public static String displayPhone(String number) {
		PhoneNumber phone = parsePhone(number);
		return phone == null ? "" : phoneUtil().format(phone, PhoneNumberFormat.NATIONAL);
	}

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
		if (dividend == null || isZero(divisor))
			return BigDecimal.ZERO;
		return dividend.divide(divisor, 4, RoundingMode.HALF_EVEN);
	}

	public static String format4Place(BigDecimal number) {
		return isZero(number) ? "" : FOUR_PLACE_DECIMAL.format(number);
	}

	public static String formatCurrency(BigDecimal number) {
		return isZero(number) ? "" : "₱" + TWO_PLACE_DECIMAL.format(number);
	}

	public static String formatPercent(BigDecimal number) {
		return isZero(number) ? "" : TWO_PLACE_DECIMAL.format(number) + "%";
	}

	public static String formatDecimal(BigDecimal number) {
		return isZero(number) ? ""
				: isZero(number.remainder(BigDecimal.ONE)) ? INTEGER.format(number) : TWO_PLACE_DECIMAL.format(number);
	}

	public static String formatId(Long number) {
		return isZero(number) ? "" : NO_COMMA_INTEGER.format(number);
	}

	public static String formatInt(Integer number) {
		return isZero(number) ? "" : INTEGER.format(number);
	}

	public static String formatPhone(PhoneNumber number) {
		return number == null || !isPhone(number) ? "" : phoneUtil().formatNumberForMobileDialing(number, "PH", true);
	}

	public static boolean isMobile(PhoneNumber phone) {
		return !isPhone(phone) ? false : phoneUtil().getNumberType(phone) == PhoneNumberType.MOBILE;
	}

	public static boolean isNegative(BigDecimal number) {
		return number == null ? false : number.compareTo(BigDecimal.ZERO) < 0;
	}

	public static boolean isNegative(Integer number) {
		return number == null ? false : number < 0;
	}

	public static boolean isNegative(Long number) {
		return number == null ? false : number < 0;
	}

	public static boolean isNegative(String string) {
		return string.startsWith("(") && string.endsWith(")");
	}

	public static boolean isPhone(PhoneNumber phone) {
		return phone == null ? false : phoneUtil().isValidNumberForRegion(phone, "PH");
	}

	public static boolean isPhone(String phone) {
		return parsePhone(phone) == null ? false : true;
	}

	public static boolean isPositive(BigDecimal number) {
		return number == null ? false : number.compareTo(BigDecimal.ZERO) > 0;
	}

	public static boolean isZero(BigDecimal number) {
		return number == null ? true : number.compareTo(BigDecimal.ZERO) == 0;
	}

	public static boolean isZero(Integer number) {
		return number == null ? true : number == 0;
	}

	public static boolean isZero(Long number) {
		return number == null ? true : number == 0;
	}

	// @formatter:off
	public static BigDecimal parseBigDecimal(String text) {
        if (text == null)
            return BigDecimal.ZERO;
        text = text.trim()
            .replace(",", "")
            .replace("(", "-")
            .replace(")","")
            .replace("₱", "");
        if (text.equals("-") || text.isEmpty())
            return BigDecimal.ZERO;
        return new BigDecimal(text);
	}
    // @formatter:on

	public static double parseDouble(String text) {
		return parseBigDecimal(text).doubleValue();
	}

	public static Integer parseInteger(String text) {
		return parseBigDecimal(text).intValue();
	}

	public static Long parseLong(String text) {
		return parseBigDecimal(text).longValue();
	}

	public static PhoneNumber parsePhone(String text) {
		try {
			PhoneNumber phone = phoneUtil().parse(text, "PH");
			return isPhone(phone) ? phone : null;
		} catch (NumberParseException e) {
			return null;
		}
	}

	public static String persistPhone(String number) {
		PhoneNumber phone = parsePhone(number);
		return phone == null ? "" : phoneUtil().format(phone, PhoneNumberFormat.E164);
	}

	public static BigDecimal toPercentRate(BigDecimal percent) {
		return percent == null ? BigDecimal.ZERO : divide(percent, Numeric.HUNDRED);
	}

	private static PhoneNumberUtil phoneUtil() {
		return PhoneNumberUtil.getInstance();
	}
}
