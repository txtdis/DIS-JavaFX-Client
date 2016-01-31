package ph.txtdis.fx.control;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

@Component
@Scope("prototype")
public class LabeledCheckBox implements InputNode<Boolean> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppCheckBox checkBox;

	private List<Node> nodes;

	private String name;

	public LabeledCheckBox build() {
		nodes = asList(label.field(name), checkBox);
		return this;
	}

	public void disableIf(ObservableBooleanValue b) {
		checkBox.disableIf(b);
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
	public BooleanBinding isEmpty() {
		return checkBox.disabledProperty().not().not();
	}

	public LabeledCheckBox name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void requestFocus() {
		checkBox.requestFocus();
	}

	@Override
	public void reset() {
		checkBox.selectedProperty().set(false);
	}

	public void setOnAction(EventHandler<ActionEvent> e) {
		checkBox.setOnAction(e);
	}
}
