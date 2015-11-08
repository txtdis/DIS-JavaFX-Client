package ph.txtdis.app;

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
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Tracked;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.dialog.AuditDialog;
import ph.txtdis.fx.dialog.OpenByDateDialog;
import ph.txtdis.fx.dialog.OpenByIdDialog;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.service.Audited;
import ph.txtdis.service.Reset;
import ph.txtdis.service.Serviced;

public abstract class AbstractIdApp<T extends Keyed<PK>, AS extends Serviced<T, PK>, PK, ID> extends AbstractApp
		implements Launchable
{
	@Autowired
	protected AS service;

	@Autowired
	protected AuditedApp auditedApp;

	@Autowired
	protected AuditDialog auditDialog;

	protected AppButton auditButton;

	@Autowired
	protected AppButton newButton;

	@Autowired
	protected AppButton backButton;

	@Autowired
	protected AppButton openByIdButton;

	@Autowired
	protected AppButton nextButton;

	@Autowired
	protected AppButton saveButton;

	@Autowired
	protected OpenByIdDialog<ID> openDialog;

	@Autowired
	protected OpenByDateDialog dateDialog;

	@Autowired
	protected AppField<String> remarksDisplay;

	@Autowired
	protected AppField<String> createdByDisplay;

	@Autowired
	protected AppField<ZonedDateTime> createdOnDisplay;

	@Autowired
	protected AppGridPane gridPane;

	protected HBox summaryBox, userHBox;

	@Override
	public void launch(String... id) {
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

	private String newModule() {
		return "New " + getHeaderText();
	}

	private void openNext() {
		try {
			service.next();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	private void openPrevious() {
		try {
			service.previous();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
	// @formatter:off
		return Arrays.asList(
			newButton.icon("new").tooltip("New entry").build(),
			backButton.icon("back").tooltip("Previous entry").build(),
			openByIdButton.icon("openByNo").tooltip("Open an entry").build(),
			nextButton.icon("next").tooltip("Next entry").build(),
			saveButton.icon("save").tooltip("Save entry").build());
	// @formatter:on
	}

	protected HBox auditPane() {
	// @formatter:off
		return box.forHorizontalPane(Arrays.asList(
			label.name("Created by"), createdByDisplay.readOnly().width(120).build(TEXT),
			label.name("on"), createdOnDisplay.readOnly().build(TIMESTAMP)));
	// @formatter:on
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
		openDialog.addParent(this).start();
		return openDialog.getId();
	}

	@Override
	protected String getHeaderText() {
		return service.getHeaderText();
	}

	@Override
	protected String getTitleText() {
		return service.getId() == null ? newModule() : service.getModuleId();
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox v = super.mainVerticalPane();
		v.getChildren().add(auditPane());
		setListeners();
		return v;
	}

	protected BooleanBinding notPosted() {
		return posted().not();
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
			launch(id);
	}

	protected BooleanExpression posted() {
		return createdByDisplay.isNotEmpty();
	}

	protected void reset() {
		((Reset) service).reset();
		refresh();
	}

	protected void saveAudit() {
		((Audited) service).setRemarks(auditDialog.getFindings());
		((Audited) service).updatePerValidity(auditDialog.isValid());
		save();
	}

	protected void setListeners() {
		newButton.setOnAction(e -> reset());
		backButton.setOnAction(e -> openPrevious());
		nextButton.setOnAction(e -> openNext());
		saveButton.setOnAction(e -> save());
		openByIdButton.setOnAction(e -> openSelected());
		setOnHidden(e -> ((Reset) service).reset());
	}

	protected void showErrorDialog(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
	}

	protected void updateLogNodes() {
		if (createdByDisplay != null) {
			createdByDisplay.setValue(((Tracked) service).getCreatedBy());
			createdOnDisplay.setValue(((Tracked) service).getCreatedOn());
		}
	}
}
