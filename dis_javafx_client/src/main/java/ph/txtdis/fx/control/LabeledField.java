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
import ph.txtdis.type.Type;

@Component
@Scope("prototype")
public class LabeledField<T> implements InputNode<T> {

	@Autowired
	private AppField<T> textField;

	@Autowired
	private LabelFactory label;

	private List<Node> nodes;

	private String name;

	public LabeledField<T> build(Type type) {
		nodes = Arrays.asList(label.field(name), textField.build(type));
		return this;
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public T getValue() {
		return textField.getValue();
	}

	@Override
	public BooleanBinding isEmpty() {
		return textField.textProperty().isEmpty();
	}

	public LabeledField<T> name(String name) {
		this.name = name;
		return this;
	}

	public LabeledField<T> readOnly() {
		textField.readOnly();
		return this;
	}

	@Override
	public void requestFocus() {
		textField.requestFocus();
	}

	@Override
	public void reset() {
		textField.clear();
	}

	public void setOnAction(EventHandler<ActionEvent> action) {
		textField.setOnAction(action);
	}

	public void setValue(T value) {
		textField.setValue(value);
	}

	public LabeledField<T> width(int width) {
		textField.width(width);
		return this;
	}
}
