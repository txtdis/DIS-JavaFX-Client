package ph.txtdis.app;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.CreationTracked;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.dialog.OpenByDateDialog;
import ph.txtdis.fx.dialog.OpenByIdDialog;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.service.Iconed;
import ph.txtdis.service.Reset;
import ph.txtdis.service.Serviced;

public abstract class AbstractIdApp<AS extends Serviced<PK>, PK, ID> extends AbstractApp implements Launchable {

	@Autowired
	protected AS service;

	@Autowired
	protected AppButton decisionButton, newButton, backButton, openByIdButton, nextButton, saveButton;

	@Autowired
	protected DecisionNeededApp decisionNeededApp;

	@Autowired
	protected OpenByIdDialog<ID> openByIdDialog;

	@Autowired
	protected OpenByDateDialog openByDateDialog;

	@Autowired
	protected AppField<String> remarksDisplay, createdByDisplay;

	@Autowired
	protected AppField<ZonedDateTime> createdOnDisplay;

	@Autowired
	protected AppGridPane gridPane;

	protected HBox summaryBox, userHBox;

	protected Boolean isNew;

	@Override
	public void actOn(String... id) {
		try {
			service.open(id[0]);
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	@Override
	public void refresh() {
		updateLogNodes();
		super.refresh();
	}

	public void save() {
		try {
			service.save();
		} catch (SuccessfulSaveInfo i) {
			dialog.show(i).addParent(this).start();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	private void openNext() {
		try {
			isNew = false;
			service.next();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	private void openPrevious() {
		try {
			isNew = false;
			service.previous();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	private String prompt() {
		return "Enter ID of " + getHeaderText() + " to open";
	}

	@Override
	protected List<AppButton> addButtons() {
		return Arrays.asList(//
				newButton.icon("new").tooltip("New entry").build(), //
				backButton.icon("back").tooltip("Previous entry").build(), //
				openByIdButton.icon("openByNo").tooltip("Open an entry").build(), //
				nextButton.icon("next").tooltip("Next entry").build(), //
				saveButton.icon("save").tooltip("Save entry").build());
	}

	protected void clearControl(InputControl<?> control) {
		control.setValue(null);
		((Node) control).requestFocus();
	}

	protected void clearControlAfterShowingErrorDialog(Exception e, InputControl<?> control) {
		showErrorDialog(e);
		clearControl(control);
	}

	protected String getDialogInput() {
		String h = service.getOpenDialogHeading();
		openByIdDialog.header(h).prompt(prompt()).addParent(this).start();
		return openByIdDialog.getId();
	}

	@Override
	protected String getFontIcon() {
		return ((Iconed) service).getFontIcon();
	}

	@Override
	protected String getHeaderText() {
		return service.getHeaderText();
	}

	@Override
	protected String getTitleText() {
		return isNew() ? newModule() : service.getModuleId() + service.getId();
	}

	protected boolean isNew() {
		if (isNew == null)
			isNew = createdOnDisplay.isEmpty().get();
		return isNew;
	}

	protected BooleanExpression isPosted() {
		return createdOnDisplay.isNotEmpty();
	}

	protected String newModule() {
		return "New " + getHeaderText();
	}

	protected BooleanBinding notPosted() {
		return isPosted().not();
	}

	protected void open(LocalDate d) {
		try {
			service.open(d);
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			actOn(id);
	}

	protected void reset() {
		((Reset) service).reset();
		isNew = null;
		refresh();
	}

	@Override
	protected void setListeners() {
		setOnHidden(e -> ((Reset) service).reset());
		newButton.setOnAction(e -> reset());
		backButton.setOnAction(e -> openPrevious());
		nextButton.setOnAction(e -> openNext());
		saveButton.setOnAction(e -> save());
		openByIdButton.setOnAction(e -> openSelected());
	}

	protected void showErrorDialog(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
	}

	protected HBox trackedPane() {
		return box.forHorizontalPane(asList(//
				label.name("Created by"), createdByDisplay.readOnly().width(120).build(TEXT), //
				label.name("on"), createdOnDisplay.readOnly().build(TIMESTAMP)));//
	}

	protected void updateLogNodes() {
		if (createdByDisplay != null) {
			createdByDisplay.setValue(((CreationTracked) service).getCreatedBy());
			createdOnDisplay.setValue(((CreationTracked) service).getCreatedOn());
		}
	}
}
