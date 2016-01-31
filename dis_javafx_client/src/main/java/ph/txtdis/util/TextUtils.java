package ph.txtdis.util;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import ph.txtdis.app.Launchable;
import ph.txtdis.app.Startable;

public class TextUtils {

	public static String blankIfNull(String s) {
		return s == null ? "" : s;
	}

	public static String capitalize(String uncapped) {
		uncapped = capitalizeFully(uncapped, '_');
		return uncapped.replace("_", " ");
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty() ? true : false;
	}

	public static String nullIfEmpty(String s) {
		return s == null || s.isEmpty() ? null : s;
	}

	public static String toBoolSign(Boolean b) {
		return b == null ? "" : b ? "√" : "x";
	}

	public static String toIdDisplay(String c) {
		return c == null ? "" : startsWith(c, "-") ? c.replace("-", "(") + ")" : c;
	}

	public static String toName(Launchable a) {
		return uncapitalize(removeAppSuffix(a));
	}

	public static String toName(Startable a) {
		return uncapitalize(removeAppSuffix(a));
	}

	public static String toString(Object o) {
		return o == null ? "" : o.toString();
	}

	private static String getClassname(Object o) {
		return o.getClass().getSimpleName();
	}

	private static String removeAppSuffix(Launchable a) {
		return removeEnd(getClassname(a), "App");
	}

	private static String removeAppSuffix(Startable a) {
		return removeEnd(getClassname(a), "App");
	}
}
