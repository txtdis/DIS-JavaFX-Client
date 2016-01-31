package ph.txtdis.fx.control;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

@Component
@Scope("prototype")
public class LabeledDatePicker implements InputNode<LocalDate> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private LocalDatePicker datePicker;

	private List<Node> nodes;

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public LocalDate getValue() {
		return datePicker.getValue();
	}

	@Override
	public BooleanBinding isEmpty() {
		return datePicker.valueProperty().isNull();
	}

	public LabeledDatePicker name(String name) {
		nodes = asList(label.field(name), datePicker);
		return this;
	}

	@Override
	public void requestFocus() {
		datePicker.requestFocus();
	}

	@Override
	public void reset() {
		datePicker.setValue(null);
	}

	public void setOnAction(EventHandler<ActionEvent> value) {
		datePicker.setOnAction(value);
	}
}
