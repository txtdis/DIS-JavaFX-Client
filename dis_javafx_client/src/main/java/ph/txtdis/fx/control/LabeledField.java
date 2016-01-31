package ph.txtdis.fx.control;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.type.Type;

@Component
@Scope("prototype")
public class LabeledField<T> implements InputControl<T>, InputNode<T> {

	@Autowired
	private AppButton searchButton;

	@Autowired
	private AppField<T> textField;

	@Autowired
	private LabelFactory label;

	private boolean isSearchable;

	private List<Node> nodes;

	private String name;

	public LabeledField<T> build(Type type) {
		nodes = asList(label.field(name), (isSearchable ? searchableField(type) : textField(type)));
		return this;
	}

	@Override
	public void clear() {
		textField.clear();
	}

	public void disableIf(BooleanBinding b) {
		textField.disableIf(b);
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

	public LabeledField<T> isSearchable() {
		isSearchable = true;
		return this;
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
		clear();
	}

	public void setOnAction(EventHandler<ActionEvent> action) {
		textField.setOnAction(action);
	}

	public void setOnSearch(EventHandler<ActionEvent> action) {
		searchButton.setOnAction(action);
	}

	@Override
	public void setValue(T value) {
		textField.setValue(value);
	}

	public LabeledField<T> width(int width) {
		textField.width(width);
		return this;
	}

	private Node searchableField(Type type) {
		return new HBox(textField(type), searchButton());
	}

	private AppButton searchButton() {
		searchButton.fontSize(16).icon("search").build();
		searchButton.focusTraversableProperty().set(false);
		return searchButton;
	}

	private AppField<T> textField(Type type) {
		return textField.build(type);
	}
}
