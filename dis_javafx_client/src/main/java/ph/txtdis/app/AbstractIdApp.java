package ph.txtdis.app;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.binding.BooleanExpression;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Audited;
import ph.txtdis.dto.Keyed;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.dialog.OpenByIdDialog;
import ph.txtdis.info.Information;
import ph.txtdis.service.AlternateNamed;
import ph.txtdis.service.Moduled;
import ph.txtdis.service.Reset;
import ph.txtdis.service.Serviced;
import ph.txtdis.service.Spun;

public abstract class AbstractIdApp<T extends Keyed<PK>, AS extends Serviced<T, PK>, PK, ID> extends AbstractApp
		implements Launchable
{

	@Autowired
	protected AS service;

	@Autowired
	protected AppButton newButton;

	@Autowired
	protected AppButton backButton;

	@Autowired
	protected AppButton openButton;

	@Autowired
	protected AppButton nextButton;

	@Autowired
	protected AppButton saveButton;

	@Autowired
	protected OpenByIdDialog<ID> openDialog;

	@Autowired
	protected AppField<String> createdByDisplay;

	@Autowired
	protected AppField<ZonedDateTime> createdOnDisplay;

	protected HBox summaryBox, userHBox;

	@Override
	public void refresh() {
		updateAuditNodes();
		super.refresh();
	}

	public void save() {
		try {
			service.save();
		} catch (Exception e) {
			showErrorDialog(e);
		} catch (Information i) {
			dialog.show(i).addParent(this).start();
		} finally {
			refresh();
		}
	}

	@Override
	public void tryOpening(String... id) {
		try {
			open(id[0]);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private String newModule() {
		return "New " + headerText();
	}

	private void open(String id) throws Exception {
		T t = service.find(id);
		service.set(t);
		refresh();
	}

	private void openNext() throws Exception {
		((Spun) service).next();
		refresh();
	}

	private void openPrevious() throws Exception {
		((Spun) service).previous();
		refresh();
	}

	private void tryNext() {
		try {
			openNext();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void tryPrevious() {
		try {
			openPrevious();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		createButtons();
		setOnButtonClickAction();
		return Arrays.asList(newButton, backButton, openButton, nextButton, saveButton);
	}

	protected HBox auditPane() {
	// @formatter:off
		return box.hpane(
			label.name("Created by"), createdByDisplay.readOnly().width(120).build(TEXT),
			label.name("on"), createdOnDisplay.readOnly().build(TIMESTAMP));
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

	protected void createButtons() {
		newButton.icon("new").tooltip("Add...").build();
		backButton.icon("back").tooltip("Back...").build();
		openButton.icon("openByNo").tooltip("Open...").build();
		nextButton.icon("next").tooltip("Next...").build();
		saveButton.icon("save").tooltip("Save...").build();
	}

	protected String getDialogInput() {
		openDialog.addParent(this).start();
		return openDialog.getId();
	}

	@Override
	protected String headerText() {
		String m = ((Moduled) service).getModule();
		return StringUtils.capitalize(m);
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox v = super.mainVerticalPane();
		v.getChildren().add(auditPane());
		setListeners();
		return v;
	}

	protected String moduleId() {
		return ((AlternateNamed) service).getModuleId() + service.getId();
	}

	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			tryOpening(id);
	}

	protected BooleanExpression posted() {
		return createdByDisplay.isNotEmpty();
	}

	protected void reset() {
		((Reset) service).reset();
		refresh();
	}

	protected void setListeners() {
		setOnHidden(e -> ((Reset) service).reset());
	}

	protected void setOnButtonClickAction() {
		newButton.setOnAction(e -> reset());
		backButton.setOnAction(e -> tryPrevious());
		openButton.setOnAction(e -> openSelected());
		nextButton.setOnAction(e -> tryNext());
		saveButton.setOnAction(e -> save());
	}

	protected void showErrorDialog(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
	}

	@Override
	protected String titleText() {
		return service.getId() == null ? newModule() : moduleId();
	}

	protected void updateAuditNodes() {
		if (createdByDisplay != null) {
			createdByDisplay.setValue(((Audited) service).getCreatedBy());
			createdOnDisplay.setValue(((Audited) service).getCreatedOn());
		}
	}
}
