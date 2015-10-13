package ph.txtdis;

import com.sun.javafx.application.LauncherImpl;

@SuppressWarnings("restriction")
public class App {

	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApp.class, AppPreloader.class, args);
	}
}
