package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgressDialog extends AbstractDialog {

	private Stage stage;

	public ProgressDialog(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void start() {
		addParent(stage);
		setScene(scene());
		show();
	}

	@Override
	protected List<Node> nodes() {
		Label label = new Label("Preparing data...");
		label.setStyle("-fx-font-size: 26pt;");

		ProgressBar bar = new ProgressBar(-1.0);
		bar.setScaleX(2.5);
		HBox barBox = new HBox(bar);
		barBox.setAlignment(Pos.CENTER);
		barBox.setPadding(new Insets(0, 0, 20, 0));

		VBox dialogBox = new VBox(label, barBox);
		return Arrays.asList(dialogBox);
	}
}
