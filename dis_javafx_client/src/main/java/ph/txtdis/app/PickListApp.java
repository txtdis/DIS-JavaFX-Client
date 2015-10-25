package ph.txtdis.app;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.PickList;
import ph.txtdis.dto.Truck;
import ph.txtdis.dto.User;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.PickListTable;
import ph.txtdis.service.PickListService;

@Lazy
@Component("pickListApp")
public class PickListApp extends AbstractIdApp<PickList, PickListService, Long, Long> {

	@Autowired
	private AppButton printButton;

	@Autowired
	private AppCombo<Truck> truckCombo;

	@Autowired
	private AppCombo<User> driverCombo;

	@Autowired
	private AppCombo<User> leadHelperCombo;

	@Autowired
	private AppCombo<User> asstHelperCombo;

	@Autowired
	private AppField<String> remarksInput;

	@Autowired
	private AppField<String> printedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> printedOnDisplay;

	@Autowired
	private AppGridPane gridPane;

	@Autowired
	private LabelFactory label;

	@Autowired
	private LocalDatePicker pickDatePicker;

	@Autowired
	private PickListTable table;

	@Override
	public void refresh() {
		pickDatePicker.setValue(service.get().getPickDate());
		truckCombo.setValue(service.get().getTruck());
		driverCombo.setValue(service.get().getDriver());
		leadHelperCombo.setValue(service.get().getLeadHelper());
		asstHelperCombo.setValue(service.get().getAsstHelper());
		remarksInput.setValue(service.get().getRemarks());
		table.items(service.get().getBookings());
		printedByDisplay.setValue(service.get().getPrintedBy());
		printedOnDisplay.setValue(service.get().getPrintedOn());
		super.refresh();
	}

	@Override
	public void setFocus() {
		pickDatePicker.requestFocus();
	}

	private AppCombo<User> asstHelperCombo() throws Exception {
		asstHelperCombo.items(service.listHelpers());
		asstHelperCombo.clear();
		return asstHelperCombo;
	}

	private AppCombo<User> driverCombo() throws Exception {
		driverCombo.items(service.listDrivers());
		driverCombo.clear();
		return driverCombo;
	}

	private void gridPane() throws Exception {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(pickDatePicker, 1, 0, 2, 1);
		gridPane.add(label.field("Truck"), 3, 0);
		gridPane.add(truckCombo(), 4, 0);
		gridPane.add(label.field("Driver"), 5, 0);
		gridPane.add(driverCombo(), 6, 0);
		gridPane.add(label.field("Lead Helper"), 7, 0);
		gridPane.add(leadHelperCombo(), 8, 0);
		gridPane.add(label.field("Asst Helper"), 9, 0);
		gridPane.add(asstHelperCombo(), 10, 0);
		gridPane.add(label.field("Remarks"), 0, 1);
		gridPane.add(remarksInput.build(TEXT), 1, 1, 10, 1);
	}

	private AppCombo<User> leadHelperCombo() throws Exception {
		leadHelperCombo.items(service.listHelpers());
		leadHelperCombo.clear();
		return leadHelperCombo;
	}

	private void print() {
		try {
			service.print();
			refresh();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private List<Node> printNodes() {
	// @formatter:off
		return Arrays.asList(
			label.name("Printed by"), printedByDisplay.readOnly().width(120).build(TEXT),
			label.name("on"), printedOnDisplay.readOnly().build(TIMESTAMP));
	// @formatter:on
	}

	private void setBindings() {
		saveButton.disableIf(table.isEmpty().or(posted()));
		printButton.disableIf(posted().not().or(printedByDisplay.isNotEmpty()));
		truckCombo.disableIf(pickDatePicker.isEmpty());
		driverCombo.disableIf(truckCombo.isEmpty());
		leadHelperCombo.disableIf(driverCombo.isEmpty());
		asstHelperCombo.disableIf(leadHelperCombo.isEmpty());
		remarksInput.disableIf(leadHelperCombo.isEmpty());
		table.disableIf(leadHelperCombo.isEmpty());
	}

	private void setDateAndUpdateTableMenuUponValidation() throws Exception {
		service.setPickDateUponValidation(pickDatePicker.getValue());
		table.addMenu();
	}

	private void setRemarks() {
		String s = remarksInput.getValue();
		if (!s.isEmpty())
			service.get().setRemarks(s);
	}

	private AppCombo<Truck> truckCombo() throws Exception {
		truckCombo.items(service.listTrucks());
		truckCombo.clear();
		return truckCombo;
	}

	private void validateAsstHelper() {
		try {
			if (service.isNew())
				service.setAsstHelperUponValidation(asstHelperCombo.getValue());
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, asstHelperCombo);
		}
	}

	private void validateDate() {
		try {
			if (pickDatePicker.getValue() != null && service.isNew())
				setDateAndUpdateTableMenuUponValidation();
		} catch (Exception e) {
			service.reset();
			refresh();
			clearControlAfterShowingErrorDialog(e, pickDatePicker);
		}
	}

	private void validateDriver() {
		try {
			if (service.isNew())
				service.setDriverUponValidation(driverCombo.getValue());
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, driverCombo);
		}
	}

	private void validateLeadHelper() {
		try {
			if (service.isNew())
				service.setLeadHelperUponValidation(leadHelperCombo.getValue());
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, leadHelperCombo);
		}
	}

	private void validateTruck() {
		try {
			if (service.isNew())
				service.setTruckUponValidation(truckCombo.getValue());
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, truckCombo);
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> c = new ArrayList<>(super.addButtons());
		printButton.icon("print").tooltip("Print...").build();
		c.add(printButton);
		return c;
	}

	@Override
	protected HBox auditPane() {
		HBox hbox = super.auditPane();
		hbox.getChildren().addAll(printNodes());
		return hbox;
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		setBindings();
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		try {
			gridPane();
		} catch (Exception e) {
			showErrorDialog(e);
		}
		return Arrays.asList(gridPane, box.hpane(table.build()));
	}

	@Override
	protected void setListeners() {
		setOnHidden(e -> service.renew());
		pickDatePicker.setOnAction(e -> validateDate());
		truckCombo.setOnAction(e -> validateTruck());
		driverCombo.setOnAction(e -> validateDriver());
		leadHelperCombo.setOnAction(e -> validateLeadHelper());
		asstHelperCombo.setOnAction(e -> validateAsstHelper());
		remarksInput.setOnAction(e -> setRemarks());
	}

	@Override
	protected void setOnButtonClickAction() {
		super.setOnButtonClickAction();
		printButton.setOnAction(e -> print());
	}
}
