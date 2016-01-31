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
import java.util.List;

import ph.txtdis.dto.StartDated;
import ph.txtdis.exception.DateInThePastException;
import ph.txtdis.exception.DuplicateException;

public class DateTimeUtils {

	public static int compareStartDates(StartDated d1, StartDated d2) {
		if (isStartDateNull(d2))
			return isStartDateNull(d1) ? 0 : 1;
		return isStartDateNull(d1) ? -1 : d1.getStartDate().compareTo(d2.getStartDate());
	}

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

	public static void validateDateIsNotInThePast(LocalDate startDate) throws DateInThePastException {
		if (startDate.isBefore(now()))
			throw new DateInThePastException();
	}

	public static void validateDateIsUnique(List<? extends StartDated> list, LocalDate startDate)
			throws DuplicateException {
		if (startDateExists(list, startDate))
			throw new DuplicateException(toDateDisplay(startDate));
	}

	public static void validateStartDate(List<? extends StartDated> list, LocalDate startDate)
			throws DateInThePastException, DuplicateException {
		validateDateIsNotInThePast(startDate);
		validateDateIsUnique(list, startDate);
	}

	private static DateTimeFormatter dateFormat() {
		return ofPattern("M/d/yyyy");
	}

	private static boolean isStartDateNull(StartDated cd) {
		return cd == null || cd.getStartDate() == null;
	}

	private static boolean startDateExists(List<? extends StartDated> list, LocalDate startDate) {
		return list.stream().filter(r -> r.getStartDate().equals(startDate)).findAny().isPresent();
	}

	private static DateTimeFormatter timestampFormat() {
		return ofPattern("M/d/yyyy h:mma");
	}

	private static ZoneId zoneHere() {
		return systemDefault();
	}
}
