package ph.txtdis.util;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Date.from;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

	public static ZonedDateTime endOfDay(LocalDate d) {
		return d == null ? null : d.plusDays(1L).atStartOfDay(zoneHere());
	}

	public static LocalDate endOfMonth(LocalDate d) {
		return startOfMonth(d).plusMonths(1L).minusDays(1L);
	}

	public static ZonedDateTime startOfDay(LocalDate d) {
		return d == null ? null : d.atStartOfDay(zoneHere());
	}

	public static LocalDate startOfMonth(LocalDate d) {
		return d == null ? now() : of(d.getYear(), d.getMonthValue(), 1);
	}

	public static LocalDate toDate(String date) {
		return date == null ? null : LocalDate.parse(date, dateFormat());
	}

	public static String toDateDisplay(LocalDate d) {
		return d == null ? "" : d.format(dateFormat());
	}

	public static String toDottedYearMonth(LocalDate d) {
		return d == null ? "" : d.format(ofPattern("yyyy.MM"));
	}

	public static String toFullMonthYear(LocalDate d) {
		return d == null ? "" : d.format(ofPattern("MMMM yyyy"));
	}

	public static String toHypenatedYearMonthDay(LocalDate d) {
		return d == null ? "" : d.format(ofPattern("yyyy-MM-dd"));
	}

	public static String toLongMonthYear(LocalDate d) {
		return d == null ? "" : d.format(ofPattern("MMM-yyyy"));
	}

	public static LocalTime toTime(String s) {
		return s == null ? null : LocalTime.parse(s, ofPattern("Hmm"));
	}

	public static String toTimeDisplay(LocalTime t) {
		return t == null ? null : t.format(ofPattern("hh:mma"));
	}

	public static String toTimestampFilename(ZonedDateTime zdt) {
		return zdt == null ? "" : zdt.withZoneSameInstant(zoneHere()).format(ofPattern("yyyy-MM-dd@hh.mma"));
	}

	public static String toTimestampText(ZonedDateTime zdt) {
		return zdt == null ? "" : zdt.withZoneSameInstant(zoneHere()).format(timestampFormat());
	}

	public static Date toUtilDate(LocalDate d) {
		return d == null ? null : from(d.atStartOfDay(zoneHere()).toInstant());
	}

	public static ZonedDateTime toZonedDateTime(String zdt) {
		return zdt == null ? null : parse(zdt, timestampFormat());
	}

	private static DateTimeFormatter dateFormat() {
		return ofPattern("M/d/yyyy");
	}

	private static DateTimeFormatter timestampFormat() {
		return ofPattern("M/d/yyyy h:mma");
	}

	private static ZoneId zoneHere() {
		return systemDefault();
	}
}
