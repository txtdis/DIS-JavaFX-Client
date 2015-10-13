package ph.txtdis.app;

import javafx.stage.Stage;

public interface Launchable {

	Startable addParent(Stage stage);

	void start();

	void tryOpening(String... id);
}