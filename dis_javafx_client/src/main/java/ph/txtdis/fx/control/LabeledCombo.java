package ph.txtdis.fx.control;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

@Component
@Scope(value = "prototype")
public class LabeledCombo<T> implements InputNode<T> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppCombo<T> comboBox;

	private List<Node> nodes;

	private String name;

	private List<T> items;

	public LabeledCombo<T> name(String name) {
		this.name = name;
		return this;
	}

	public LabeledCombo<T> items(T[] types) {
		return items(Arrays.asList(types));
	}

	public LabeledCombo<T> items(List<T> items) {
		this.items = items;
		return this;
	}

	public LabeledCombo<T> build() {
		nodes = Arrays.asList(label.field(name), comboBox.items(items));
		return this;
	}

	public void setOnAction(EventHandler<ActionEvent> event) {
		comboBox.setOnAction(event);
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public T getValue() {
		return comboBox.getValue();
	}

	@Override
	public void reset() {
		comboBox.setValue(null);
	}

	@Override
	public void requestFocus() {
		comboBox.requestFocus();
	}

	@Override
	public BooleanBinding isEmpty() {
		return comboBox.isEmpty();
	}

	public void select(T item) {
		comboBox.select(item);
	}

	public void select(int index) {
		comboBox.select(index);
	}
}
