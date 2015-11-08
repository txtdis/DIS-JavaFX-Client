package ph.txtdis;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JavaTest {

	public static void main(String[] args) {
		LocalTime t = LocalTime.parse("1309", DateTimeFormatter.ofPattern("Hmm"));
		System.out.println(t);
	}
}
