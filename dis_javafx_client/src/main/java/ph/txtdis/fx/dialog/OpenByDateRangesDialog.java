package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;

@Scope("prototype")
@Component("openByDateRangesDialog")
public class OpenByDateRangesDialog extends InputDialog {

	private class EndDateBeforeStartException extends Exception {

		private static final long serialVersionUID = 2374761504558474009L;

		public EndDateBeforeStartException() {
			super("End date cannot be before start");
		}
	}

	private class NoStartDateException extends Exception {

		private static final long serialVersionUID = 2934631533711430249L;

		public NoStartDateException() {
			super("Enter a start date first");
		}
	}

	@Autowired
	protected AppGridPane grid;

	@Autowired
	protected AppButton actionButton;

	@Autowired
	private LocalDatePicker startDatePicker, endDatePicker;

	private LocalDate startDate, endDate;

	private String actionButtonText = "Generate";

	public LocalDate getEndDate() {
		return endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	@Override
	public void refresh() {
		startDatePicker.setValue(null);
		endDatePicker.setValue(null);
		super.refresh();
	}

	public OpenByDateRangesDialog setActionButtonText(String t) {
		actionButtonText = t;
		return this;
	}

	@Override
	public void setFocus() {
		startDatePicker.requestFocus();
	}

	private Node endDatePicker() {
		endDatePicker.setOnAction(e -> validateDates());
		return endDatePicker;
	}

	private void ensureDateIsNotBeforeStart() throws NoStartDateException, EndDateBeforeStartException {
		LocalDate start = startDatePicker.getValue();
		if (start == null)
			throw new NoStartDateException();
		if (endDatePicker.getValue().isBefore(start))
			throw new EndDateBeforeStartException();
	}

	private void resetNodesOnError(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		refresh();
	}

	private void setDates() {
		startDate = startDatePicker.getValue();
		endDate = endDatePicker.getValue();
		refresh();
		close();
	}

	private void validateDates() {
		if (endDatePicker.getValue() != null)
			try {
				ensureDateIsNotBeforeStart();
			} catch (Exception e) {
				resetNodesOnError(e);
			}
	}

	protected Button actionButton() {
		actionButton.large(actionButtonText).build();
		actionButton.setOnAction(event -> setDates());
		actionButton.disableIf(startDatePicker.isEmpty().or(endDatePicker.isEmpty()));
		return actionButton;
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { actionButton(), closeButton() };
	}

	@Override
	protected List<Node> nodes() {
		grid.getChildren().clear();
		grid.add(label.field("Start"), 0, 0);
		grid.add(startDatePicker, 1, 0);
		grid.add(label.field("End"), 0, 1);
		grid.add(endDatePicker(), 1, 1);
		return asList(header(), grid, buttonBox());
	}

	@Override
	protected void setOnFiredCloseButton() {
		startDate = null;
		endDate = null;
		refresh();
		super.setOnFiredCloseButton();
	}
}
