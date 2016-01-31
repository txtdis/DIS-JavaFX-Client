package ph.txtdis.fx.pane;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@Component
public class AppBoxPaneFactory {

	public HBox forGridGroup(Node... n) {
		HBox b = forHorizontals(n);
		b.setSpacing(10);
		b.setAlignment(Pos.CENTER_LEFT);
		return b;
	}

	public HBox forHorizontalPane(List<? extends Node> n) {
		HBox b = forHorizontals(n);
		b.setSpacing(10);
		b.setPadding(new Insets(0, 10, 10, 10));
		b.setAlignment(Pos.CENTER);
		return b;
	}

	public HBox forHorizontalPane(Node... n) {
		return forHorizontalPane(Arrays.asList(n));
	}

	public HBox forHorizontals(List<? extends Node> n) {
		return new HBox(n.toArray(new Node[n.size()]));
	}

	public HBox forHorizontals(Node... n) {
		return new HBox(n);
	}

	public HBox forIdName(Node... n) {
		HBox b = forHorizontals(n);
		b.setSpacing(10);
		b.setAlignment(Pos.CENTER_LEFT);
		return b;
	}

	public HBox forSubheader(Label l) {
		HBox b = forHorizontals(l);
		b.setPadding(new Insets(20, 0, 0, 0));
		b.setAlignment(Pos.CENTER);
		return b;
	}

	public HBox forTableTotals(List<? extends Node> totalDisplays) {
		HBox b = forHorizontals(totalDisplays);
		b.setPadding(new Insets(0, 20, 0, 0));
		b.setAlignment(Pos.CENTER_RIGHT);
		return b;
	}

	public VBox forVerticals(List<Node> n) {
		return new VBox(n.toArray(new Node[n.size()]));
	}

	public VBox forVerticals(Node... n) {
		return new VBox(n);
	}
}
