package ph.txtdis.app;

import javafx.stage.Stage;

public interface Launchable {

	void actOn(String... id);

	Startable addParent(Stage stage);

	void start();
}
