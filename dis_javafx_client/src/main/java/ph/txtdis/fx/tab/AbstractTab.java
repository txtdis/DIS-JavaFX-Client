package ph.txtdis.fx.tab;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.fx.pane.AppBoxPaneFactory;
import ph.txtdis.fx.pane.AppGridPane;

public abstract class AbstractTab extends Tab implements InputTab {

	@Autowired
	protected AppBoxPaneFactory box;

	@Autowired
	protected MessageDialog dialog;

	@Autowired
	protected AppGridPane gridPane;

	public AbstractTab(String name) {
		setText(name);
	}

	@Override
	public AbstractTab asTab() {
		return this;
	}

	public InputTab build() {
		setContent(mainVerticalPane());
		return this;
	}

	public void disableIf(ObservableBooleanValue b) {
		disableProperty().bind(b);
	}

	public ObservableBooleanValue isDisabledNow() {
		return disabledProperty();
	}

	public void select() {
		getTabPane().getSelectionModel().select(this);
	}

	protected VBox mainVerticalPane() {
		VBox vbox = box.vbox();
		vbox.getChildren().addAll(mainVerticalPaneNodes());
		return vbox;
	}

	protected abstract List<Node> mainVerticalPaneNodes();
}
