package ph.txtdis.fx.dialog;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.NoArgsConstructor;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.DialogButtonBox;

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

	protected String heading, prompt;

	public InputDialog header(String heading) {
		this.heading = heading;
		return this;
	}

	public InputDialog prompt(String prompt) {
		this.prompt = prompt;
		return this;
	}

	@Override
	public void refresh() {
		setFocus();
	}

	protected Button closeButton(String name) {
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
		return closeButton("Close");
	}

	protected Label header() {
		return label.dialog(headerText());
	}

	protected String headerText() {
		return heading;
	}

	protected void setOnFiredCloseButton() {
		close();
	}
}
