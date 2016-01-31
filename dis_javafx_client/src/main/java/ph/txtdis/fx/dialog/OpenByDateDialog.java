package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;

@Scope("prototype")
@Component("openByDateDialog")
public class OpenByDateDialog extends InputDialog {

	@Autowired
	protected AppGridPane grid;

	@Autowired
	private LocalDatePicker datePicker;

	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}

	@Override
	public void setFocus() {
		datePicker.requestFocus();
	}

	private LocalDatePicker datePicker() {
		datePicker.setValue(null);
		datePicker.setOnAction(e -> onPick());
		return datePicker;
	}

	private void onPick() {
		date = datePicker.getValue();
		close();
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { closeButton() };
	}

	@Override
	protected List<Node> nodes() {
		Label help = new Label(prompt);
		grid.getChildren().clear();
		grid.add(help, 0, 0);
		grid.add(datePicker(), 0, 1);
		return asList(header(), grid, buttonBox());
	}

	@Override
	protected void setOnFiredCloseButton() {
		date = null;
		datePicker.setValue(date);
		super.setOnFiredCloseButton();
	}
}
