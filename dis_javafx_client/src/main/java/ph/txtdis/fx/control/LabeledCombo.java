package ph.txtdis.fx.control;

import static java.util.Arrays.asList;
import static javafx.collections.FXCollections.emptyObservableList;
import static javafx.collections.FXCollections.observableArrayList;

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

	public LabeledCombo<T> build() {
		nodes = asList(label.field(name), comboBox);
		return this;
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public T getValue() {
		return comboBox.getValue();
	}

	public BooleanBinding is(T t) {
		return comboBox.is(t);
	}

	@Override
	public BooleanBinding isEmpty() {
		return comboBox.isEmpty();
	}

	public LabeledCombo<T> items(List<T> items) {
		comboBox.setItems(items == null ? emptyObservableList() : observableArrayList(items));
		comboBox.select(0);
		return this;
	}

	public LabeledCombo<T> items(T[] types) {
		return items(Arrays.asList(types));
	}

	public LabeledCombo<T> name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void requestFocus() {
		comboBox.requestFocus();
	}

	@Override
	public void reset() {
		comboBox.setValue(null);
	}

	public void select(int index) {
		comboBox.select(index);
	}

	public void select(T item) {
		comboBox.select(item);
	}

	public void setOnAction(EventHandler<ActionEvent> event) {
		comboBox.setOnAction(event);
	}
}
