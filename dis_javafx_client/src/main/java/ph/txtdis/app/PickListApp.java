package ph.txtdis.app;

import static java.util.Arrays.asList;
import static javafx.beans.binding.Bindings.when;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.PickList;
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
public class PickListApp extends AbstractIdApp<PickListService, Long, Long> {

	@Autowired
	private AppButton printButton;

	@Autowired
	private AppCombo<String> truckCombo;

	@Autowired
	private AppCombo<String> driverCombo;

	@Autowired
	private AppCombo<String> leadHelperCombo;

	@Autowired
	private AppCombo<String> asstHelperCombo;

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
		super.refresh();
		pickDatePicker.setValue(pickList().getPickDate());
		truckCombo.items(service.listTrucks());
		refreshSucceedingControls();
	}

	@Override
	public void setFocus() {
		pickDatePicker.requestFocus();
	}

	private void createTableContextMenuUponTruckValidation() {
		if (truckCombo.getItems() != null && truckCombo.getItems().size() > 1)
			try {
				service.setTruckUponValidation(truckCombo.getValue());
				refreshSucceedingControls();
				table.addMenu();
			} catch (Exception e) {
				clearControlAfterShowingErrorDialog(e, truckCombo);
			}
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(pickDatePicker, 1, 0, 2, 1);
		gridPane.add(label.field("Truck"), 3, 0);
		gridPane.add(truckCombo, 4, 0);
		gridPane.add(label.field("Driver"), 5, 0);
		gridPane.add(driverCombo, 6, 0);
		gridPane.add(label.field("Lead Helper"), 7, 0);
		gridPane.add(leadHelperCombo, 8, 0);
		gridPane.add(label.field("Asst Helper"), 9, 0);
		gridPane.add(asstHelperCombo, 10, 0);
		gridPane.add(label.field("Remarks"), 0, 1);
		gridPane.add(remarksInput.build(TEXT), 1, 1, 10, 1);
		return gridPane;
	}

	private BooleanBinding ifPickUpThenOnEmptyDateElseOnLeadHelper() {
		return when(isPickup())//
				.then(noPickDate())//
				.otherwise(noLeadHelper());
	}

	private BooleanBinding noLeadHelper() {
		return leadHelperCombo.isEmpty();
	}

	private BooleanBinding noPickDate() {
		return pickDatePicker.valueProperty().isNull();
	}

	private PickList pickList() {
		return service.get();
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
		return asList(//
				label.name("Printed by"), printedByDisplay.readOnly().width(120).build(TEXT), //
				label.name("on"), printedOnDisplay.readOnly().build(TIMESTAMP));
	}

	private void refreshSucceedingControls() {
		driverCombo.items(service.listDrivers());
		leadHelperCombo.items(service.listLeadHelpers());
		asstHelperCombo.items(service.listAsstHelpers());
		remarksInput.setValue(pickList().getRemarks());
		table.items(pickList().getBookings());
		printedByDisplay.setValue(pickList().getPrintedBy());
		printedOnDisplay.setValue(pickList().getPrintedOn());
	}

	private void setRemarks() {
		String s = remarksInput.getValue();
		if (!s.isEmpty())
			pickList().setRemarks(s);
	}

	private void validateAsstHelper() {
		if (asstHelperCombo.getItems() != null && asstHelperCombo.getItems().size() > 1)
			try {
				service.setAsstHelperUponValidation(asstHelperCombo.getValue());
			} catch (Exception e) {
				clearControlAfterShowingErrorDialog(e, asstHelperCombo);
			}
	}

	private void validateDate() {
		if (!noPickDate().get() && isNew())
			try {
				service.setPickDateUponValidation(pickDatePicker.getValue());
			} catch (Exception e) {
				service.reset();
				refresh();
				clearControlAfterShowingErrorDialog(e, pickDatePicker);
			}
	}

	private void validateDriver() {
		if (driverCombo.getItems() != null && driverCombo.getItems().size() > 1)
			try {
				service.setDriverUponValidation(driverCombo.getValue());
			} catch (Exception e) {
				clearControlAfterShowingErrorDialog(e, driverCombo);
			}
	}

	private void validateLeadHelper() {
		if (leadHelperCombo.getItems() != null && leadHelperCombo.getItems().size() > 1)
			try {
				service.setLeadHelperUponValidation(leadHelperCombo.getValue());
			} catch (Exception e) {
				clearControlAfterShowingErrorDialog(e, leadHelperCombo);
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
	protected List<Node> mainVerticalPaneNodes() {
		return asList(gridPane(), box.forHorizontalPane(table.build()), trackedPane());
	}

	@Override
	protected void reset() {
		truckCombo.empty();
		driverCombo.empty();
		leadHelperCombo.empty();
		asstHelperCombo.empty();
		super.reset();
	}

	@Override
	protected void setBindings() {
		saveButton.disableIf(table.isEmpty()//
				.or(table.disabledProperty())//
				.or(isPosted()));
		printButton.disableIf(isPosted().not()//
				.or(printedByDisplay.isNotEmpty()));
		truckCombo.disableIf(noPickDate());
		driverCombo.disableIf(truckCombo.isEmpty()//
				.or(isPickup()));
		leadHelperCombo.disableIf(driverCombo.isEmpty());
		asstHelperCombo.disableIf(noLeadHelper());
		remarksInput.disableIf(ifPickUpThenOnEmptyDateElseOnLeadHelper());
		table.disableIf(ifPickUpThenOnEmptyDateElseOnLeadHelper());
	}

	private BooleanBinding isPickup() {
		return truckCombo.is("PICK-UP");
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		pickDatePicker.setOnAction(e -> validateDate());
		truckCombo.setOnAction(e -> createTableContextMenuUponTruckValidation());
		driverCombo.setOnAction(e -> validateDriver());
		leadHelperCombo.setOnAction(e -> validateLeadHelper());
		asstHelperCombo.setOnAction(e -> validateAsstHelper());
		remarksInput.setOnAction(e -> setRemarks());
		printButton.setOnAction(e -> print());
	}

	@Override
	protected HBox trackedPane() {
		HBox hbox = super.trackedPane();
		hbox.getChildren().addAll(printNodes());
		return hbox;
	}
}
