package ph.txtdis.fx.pane;

import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Component
public class AppBoxPaneFactory {

	public HBox gridpane(Node... nodes) {
		HBox box = hbox(nodes);
		box.setSpacing(10);
		box.setAlignment(Pos.CENTER_LEFT);
		return box;
	}

	public HBox hbox(Node... nodes) {
		return new HBox(nodes);
	}

	public HBox hpane(Node... nodes) {
		HBox box = hbox(nodes);
		box.setSpacing(10);
		box.setPadding(new Insets(0, 10, 10, 10));
		box.setAlignment(Pos.CENTER);
		return box;
	}

	public VBox vbox(Node... nodes) {
		return new VBox(nodes);
	}
}
