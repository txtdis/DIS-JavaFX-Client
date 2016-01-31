package ph.txtdis.fx.tab;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.control.ErrorHandling;
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
		setContent(null);
	}

	@Override
	public AbstractTab asTab() {
		return this;
	}

	public InputTab build() {
		setContent(mainVerticalPane());
		setListeners();
		setBindings();
		return this;
	}

	public void disableIf(ObservableBooleanValue b) {
		disableProperty().bind(b);
	}

	public void select() {
		getTabPane().getSelectionModel().select(this);
	}

	protected void handleError(ErrorHandling control, Exception e) {
		dialog.show(e).addParent(this).start();
		control.handleError();
	}

	protected VBox mainVerticalPane() {
		return box.forVerticals(mainVerticalPaneNodes());
	}

	protected abstract List<Node> mainVerticalPaneNodes();

	protected void setBindings() {
	}

	protected void setListeners() {
	}
}
