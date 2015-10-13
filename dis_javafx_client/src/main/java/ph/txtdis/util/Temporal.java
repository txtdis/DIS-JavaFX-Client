package ph.txtdis.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Temporal {

	public static Date toUtilDate(LocalDate d) {
		return d == null ? null : Date.from(d.atStartOfDay(zoneHere()).toInstant());
	}

	public static String format(LocalDate d) {
		return d == null ? "" : d.format(dateFormat());
	}

	public static String format(ZonedDateTime zdt) {
		return zdt == null ? "" : zdt.withZoneSameInstant(zoneHere()).format(timestampFormat());
	}

	public static String toFullMonthYear(LocalDate d) {
		return d == null ? "" : d.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
	}

	public static String toLongMonthYear(LocalDate d) {
		return d == null ? "" : d.format(DateTimeFormatter.ofPattern("MMM-yyyy"));
	}

	public static String toFileMonthYear(LocalDate d) {
		return d == null ? "" : d.format(DateTimeFormatter.ofPattern("yyyy.MM"));
	}

	public static String toFilename(LocalDate d) {
		return d == null ? "" : d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public static String toFilename(ZonedDateTime zdt) {
		return zdt == null ? "" : zdt.withZoneSameInstant(zoneHere()).format(timestampFile());
	}

	public static ZonedDateTime toZonedDateTime(String zdt) {
		return zdt == null ? null : ZonedDateTime.parse(zdt, timestampFormat());
	}

	public static LocalDate toLocalDate(String date) {
		return date == null ? null : LocalDate.parse(date, dateFormat());
	}

	public static ZonedDateTime startOfDay(LocalDate d) {
		return d == null ? null : d.atStartOfDay(zoneHere());
	}

	public static ZonedDateTime endOfDay(LocalDate d) {
		return d == null ? null : d.plusDays(1L).atStartOfDay(zoneHere());
	}

	public static LocalDate startOfMonth(LocalDate d) {
		return d == null ? LocalDate.now() : LocalDate.of(d.getYear(), d.getMonthValue(), 1);
	}

	public static LocalDate endOfMonth(LocalDate d) {
		return startOfMonth(d).plusMonths(1L).minusDays(1L);
	}

	private static DateTimeFormatter dateFormat() {
		return DateTimeFormatter.ofPattern("M/d/yyyy");
	}

	private static DateTimeFormatter timestampFormat() {
		return DateTimeFormatter.ofPattern("M/d/yyyy h:mma");
	}

	private static DateTimeFormatter timestampFile() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd@hh.mma");
	}

	private static ZoneId zoneHere() {
		return ZoneId.systemDefault();
	}
}
