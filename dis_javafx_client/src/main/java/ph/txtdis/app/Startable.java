package ph.txtdis.app;

import javafx.stage.Stage;

public interface Startable {

	void start();

	void refresh();

	void setFocus();

	Startable addParent(Stage stage);
}