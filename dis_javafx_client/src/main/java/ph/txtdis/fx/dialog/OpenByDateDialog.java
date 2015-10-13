package ph.txtdis.fx.dialog;

import java.time.LocalDate;
import java.util.Arrays;
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

	private String headerText, criteria;

	public OpenByDateDialog criteria(String criteria) {
		this.criteria = criteria;
		return this;
	}

	public LocalDate getDate() {
		return date;
	}

	public OpenByDateDialog header(String headerText) {
		this.headerText = headerText;
		return this;
	}

	@Override
	public void setFocus() {
		datePicker.requestFocus();
	}

	private LocalDatePicker datePicker() {
		datePicker.setOnAction(e -> onPick());
		return datePicker;
	}

	private void onPick() {
		date = datePicker.getValue();
		datePicker.clear();
		close();
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { closeButton() };
	}

	@Override
	protected String headerText() {
		return headerText;
	}

	@Override
	protected List<Node> nodes() {
		Label help = new Label(criteria);
		grid.getChildren().clear();
		grid.add(help, 0, 0);
		grid.add(datePicker(), 0, 1);
		return Arrays.asList(header(), grid, buttonBox());
	}
}
