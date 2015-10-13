package ph.txtdis.fx.control;

import java.time.LocalDate;
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
@Scope("prototype")
public class LabeledDatePicker implements InputNode<LocalDate> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private LocalDatePicker datePicker;

	private List<Node> nodes;

	public LabeledDatePicker name(String name) {
		nodes = Arrays.asList(label.field(name), datePicker);
		return this;
	}

	public void setOnAction(EventHandler<ActionEvent> value) {
		datePicker.setOnAction(value);
	}

	@Override
	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public LocalDate getValue() {
		return datePicker.getValue();
	}

	@Override
	public void reset() {
		datePicker.setValue(null);
	}

	@Override
	public void requestFocus() {
		datePicker.requestFocus();
	}

	@Override
	public BooleanBinding isEmpty() {
		return datePicker.valueProperty().isNull();
	}
}
