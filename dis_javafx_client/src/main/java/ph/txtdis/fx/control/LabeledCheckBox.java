package ph.txtdis.fx.control;

import java.util.Arrays;
import java.util.List;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class LabeledCheckBox implements InputNode<Boolean> {
	private final List<Node> nodes;
	private final CheckBox checkBox;

	public LabeledCheckBox(String name) {
		Label label = new Label(name);
		checkBox = new CheckBox();
		nodes = Arrays.asList(label, checkBox);
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public Boolean getValue() {
		return checkBox.selectedProperty().get();
	}

	@Override
	public void reset() {
		checkBox.selectedProperty().set(false);
	}

	@Override
	public void requestFocus() {
		checkBox.requestFocus();
	}

	@Override
	public BooleanBinding isEmpty() {
		return checkBox.disabledProperty().not().not();
	}
}
