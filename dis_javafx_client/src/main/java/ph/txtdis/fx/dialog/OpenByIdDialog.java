package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import ph.txtdis.app.Startable;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.type.Type;
import ph.txtdis.util.Text;

@Scope("prototype")
@Component("openByIdDialog")
public class OpenByIdDialog<PK> extends InputDialog {

	@Autowired
	private AppGridPane grid;

	@Autowired
	private AppButton openButton;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<PK> idField;

	private PK id;

	public String getId() {
		return id.toString();
	}

	@Override
	public void setFocus() {
		idField.requestFocus();
	}

	private Type getType() {
		return id instanceof Long ? ID : TEXT;
	}

	private Button openButton() {
		openButton.large("Open").build();
		openButton.setOnAction(event -> setEnteredId());
		openButton.disableIf(idField.isEmpty());
		return openButton;
	}

	private void setEnteredId() {
		id = idField.getValue();
		idField.clear();
		close();
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { openButton(), closeButton() };
	}

	@Override
	protected String headerText() {
		return "Open " + Text.toHeader((Startable) getOwner()) + " Data";
	}

	@Override
	protected List<Node> nodes() {
		grid.getChildren().clear();
		grid.add(label.field("ID No"), 0, 0);
		grid.add(idField.build(getType()), 1, 0);
		return Arrays.asList(header(), grid, buttonBox());
	}
}
