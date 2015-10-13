package ph.txtdis.fx.control;

import java.util.Arrays;
import java.util.List;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;

public class LabeledTextArea implements InputNode<String> {

	private final List<Node> nodes;
	private final TextArea textArea;

	public LabeledTextArea(String name) {
		Label label = new Label(name);
		textArea = new TextArea();
		textArea.setTooltip(new Tooltip("Use tab to traverse"));
		nodes = Arrays.asList(label, textArea);
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public void reset() {
		textArea.clear();
	}

	@Override
	public void requestFocus() {
		textArea.requestFocus();
	}

	@Override
	public BooleanBinding isEmpty() {
		return textArea.textProperty().isEmpty();
	}

	protected String getText() {
		return textArea.getText();
	}

	@Override
	public String getValue() {
		return textArea.getText();
	}
}
