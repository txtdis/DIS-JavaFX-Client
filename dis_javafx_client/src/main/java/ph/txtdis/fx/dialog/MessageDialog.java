package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static org.apache.log4j.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

	private static Logger logger = getLogger(MessageDialog.class);

	@Autowired
	private AppButton closeButton, optionButton;

	@Autowired
	private LabelFactory label;

	@Autowired
	private MessageDialogButtonBox buttonBox;

	@Autowired
	private DialogMessageBox messageBox;

	private String text, unicode, closeText, color, optionText;

	private boolean withOption;

	public List<Button> buttons() {
		List<Button> l = new ArrayList<>();
		if (withOption)
			l.add(optionButton());
		l.add(closeButton());
		return l;
	}

	@Override
	public void refresh() {
		setFocus();
	}

	public void setErrorStyle() {
	}

	@Override
	public void setFocus() {
		if (withOption)
			optionButton.requestFocus();
		else
			closeButton.requestFocus();
	}

	public void setOnDefaultSelection(EventHandler<ActionEvent> e) {
		closeButton.setOnAction(e);
	}

	public void setOnOptionSelection(EventHandler<ActionEvent> e) {
		optionButton.setOnAction(e);
	}

	public MessageDialog show(Exception e) {
		return showError(e.getMessage());
	}

	public MessageDialog show(Information i) {
		return showInfo(i.getMessage());
	}

	public MessageDialog showError(String error) {
		text = error;
		withOption = false;
		unicode = "\ue80f";
		color = "maroon";
		closeText = "OK";
		return this;
	}

	public MessageDialog showInfo(String info) {
		text = info;
		withOption = false;
		unicode = "\ue813";
		color = "lime";
		closeText = "OK";
		return this;
	}

	public MessageDialog showOption(String text, String option, String close) {
		this.text = text;
		this.optionText = option;
		this.closeText = close;
		this.withOption = true;
		unicode = "\ue916";
		color = "yellow";
		return this;
	}

	@Override
	public void start() {
		super.start();
		logger.info("Option button label = " + optionButton.getText());
		logger.info("Close button label = " + closeButton.getText());
	}

	private Button closeButton() {
		closeButton.large(closeText).build();
		if (!withOption)
			closeButton.setOnAction(e -> close());
		return closeButton;
	}

	private Button optionButton() {
		return optionButton.large(optionText).build();
	}

	@Override
	protected List<Node> nodes() {
		buttonBox.addButtons(buttons());
		messageBox.addNodes(label.message(text), buttonBox);
		return asList(new HBox(label.icon(unicode, color), messageBox));
	}
}
