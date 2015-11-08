package ph.txtdis.util;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;

import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import ph.txtdis.app.Launchable;
import ph.txtdis.app.Startable;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.dialog.AbstractDialog;
import ph.txtdis.type.ModuleType;

public class TextUtils {

	public static String capitalize(String uncapped) {
		uncapped = capitalizeFully(uncapped, '_');
		return uncapped.replace("_", " ");
	}

	public static boolean isEmpty(String s) {
		return s == null || s.isEmpty() ? true : false;
	}

	public static String nameFromService(Object o) {
		return uncapitalize(removeServiceSuffix(o));
	}

	public static String toHeader(AbstractDialog d) {
		return removeEnd(getClassname(d), "Dialog");
	}

	public static String toHeader(ModuleType t) {
		return capitalizeFully(t.toString(), '_').replace("_", " ");
	}

	public static String toHeader(Startable app) {
		return capitalize(toSpaced(app));
	}

	public static String toName(AppButton b) {
		return uncapitalize(removeButtonSuffix(b));
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

	private static String removeButtonSuffix(AppButton b) {
		return removeEnd(getClassname(b), "Button");
	}

	private static String removeServiceSuffix(Object o) {
		return removeEnd(getClassname(o), "Service");
	}

	private static String[] spacedReplacements() {
		return new String[] { " A", " B", " C", " D", " E", " F", " G", " H", " I", " J", " K", " L", " M", " N", " O",
				" P", " Q", " R", " S", " T", " U", " V", " W", " X", " Y", " Z" };
	}

	private static String toSpaced(Startable app) {
		return replaceEach(toName(app), upperCaseSearches(), spacedReplacements());
	}

	private static String[] upperCaseSearches() {
		return new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
	}
}
