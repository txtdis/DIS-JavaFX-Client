package ph.txtdis.app;

import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Picking;
import ph.txtdis.dto.Truck;
import ph.txtdis.dto.User;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.PickTable;
import ph.txtdis.info.Information;
import ph.txtdis.service.PickService;

@Lazy
@Component("pickingApp")
public class PickingApp extends AbstractIdApp<Picking, PickService, Long, Long> {

	@Autowired
	private LabelFactory label;

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
	private LocalDatePicker pickDatePicker;

	@Autowired
	private AppGridPane gridPane;

	@Autowired
	private PickTable table;

	@Override
	public void refresh() {
		pickDatePicker.setValue(service.get().getPickDate());
		remarksInput.setValue(service.get().getRemarks());
		table.items(service.get().getBookings());
		super.refresh();
	}

	@Override
	public void save() {
		try {
			service.save();
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
			e.printStackTrace();
		} catch (Information i) {
			dialog.show(i).addParent(this).start();
			refresh();
		}
	}

	@Override
	public void setFocus() {
		newButton.requestFocus();
	}

	private void handleError(Exception e, InputControl<?> control) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		control.setValue(null);
		((Node) control).requestFocus();
	}

	private void setBindings() {
		saveButton.disableIf(table.isEmpty().or(posted()));
		truckCombo.disableIf(pickDatePicker.isEmpty().or(posted()));
		driverCombo.disableIf(truckCombo.isEmpty().or(posted()));
		leadHelperCombo.disableIf(driverCombo.isEmpty().or(posted()));
		asstHelperCombo.disableIf(leadHelperCombo.isEmpty().or(posted()));
		remarksInput.disableIf(leadHelperCombo.isEmpty().or(posted()));
		table.disableIf(leadHelperCombo.isEmpty().or(posted()));
	}

	private void updateUponDateValidation() {
		try {
			service.setOrderDateUponValidation(pickDatePicker.getValue());
		} catch (Exception e) {
			handleError(e, pickDatePicker);
		}
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		setBindings();
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
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
		gridPane.add(remarksInput.build(TEXT), 1, 1, 9, 1);
		return Arrays.asList(gridPane, box.hpane(table.build()));
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		pickDatePicker.setOnAction(a -> updateUponDateValidation());
		remarksInput.setOnAction(a -> service.get().setRemarks(remarksInput.getValue()));
	}
}
