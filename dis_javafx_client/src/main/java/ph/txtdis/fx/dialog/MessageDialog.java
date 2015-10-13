package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.DialogMessageBox;
import ph.txtdis.fx.pane.MessageDialogButtonBox;
import ph.txtdis.info.Information;

@Scope("prototype")
@Component("messageDialog")
public class MessageDialog extends AbstractDialog {

	@Autowired
	private AppButton okButton;

	@Autowired
	private LabelFactory label;

	@Autowired
	private MessageDialogButtonBox buttonBox;

	@Autowired
	private DialogMessageBox messageBox;

	private String text, unicode, color;

	@Override
	public void refresh() {
		setFocus();
	}

	@Override
	public void setFocus() {
		okButton.requestFocus();
	}

	public MessageDialog show(Exception e) {
		return showError(e.getMessage());
	}

	public MessageDialog show(Information i) {
		return showInfo(i.getMessage());
	}

	public MessageDialog showError(String error) {
		text = error;
		unicode = "\ue80f";
		color = "maroon";
		return this;
	}

	public MessageDialog showInfo(String info) {
		text = info;
		unicode = "\ue813";
		color = "lime";
		return this;
	}

	private Button okButton() {
		okButton.large("OK").build();
		okButton.setOnAction(c -> close());
		return okButton;
	}

	@Override
	protected List<Node> nodes() {
		buttonBox.addButtons(okButton());
		messageBox.addNodes(label.message(text), buttonBox);
		return Arrays.asList(new HBox(label.icon(unicode, color), messageBox));
	}
}
