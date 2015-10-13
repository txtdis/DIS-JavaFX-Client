package ph.txtdis.util;

public class Util {

	public static boolean areEqual(Object first, Object second) {
		return first == null ? first == second : first.equals(second);
	}
}
