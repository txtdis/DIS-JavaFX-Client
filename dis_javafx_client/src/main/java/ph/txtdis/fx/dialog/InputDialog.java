package ph.txtdis.fx.dialog;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.NoArgsConstructor;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.DialogButtonBox;
import ph.txtdis.util.Text;

@NoArgsConstructor
public abstract class InputDialog extends AbstractDialog {

	@Autowired
	protected MessageDialog dialog;

	@Autowired
	protected AppButton closeButton;

	@Autowired
	protected LabelFactory label;

	@Autowired
	protected DialogButtonBox box;

	@Override
	public void refresh() {
		setFocus();
	}

	protected Button button(String name) {
		closeButton.large(name).build();
		closeButton.setOnAction(event -> setOnFiredCloseButton());
		return closeButton;
	}

	protected HBox buttonBox() {
		return box.addButtons(buttons());
	}

	protected Button[] buttons() {
		return new Button[] { closeButton() };
	}

	protected Button closeButton() {
		return button("Close");
	}

	protected Label header() {
		return label.dialog(headerText());
	}

	protected String headerText() {
		return Text.toHeader(this);
	}

	protected void setOnFiredCloseButton() {
		close();
	}
}
