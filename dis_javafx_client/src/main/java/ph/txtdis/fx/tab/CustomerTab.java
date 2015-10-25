package ph.txtdis.fx.tab;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.Location;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.ErrorHandling;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.table.RoutingTable;
import ph.txtdis.service.CustomerService;
import ph.txtdis.type.CustomerType;
import ph.txtdis.type.VisitFrequency;

@Lazy
@Component("customerTab")
public class CustomerTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<Long> idField;

	@Autowired
	private AppField<String> nameField;

	@Autowired
	private AppField<Long> parentIdField;

	@Autowired
	private AppField<String> parentDisplay;

	@Autowired
	private AppField<String> streetField;

	@Autowired
	private AppCombo<Location> provinceCombo;

	@Autowired
	private AppCombo<Location> cityCombo;

	@Autowired
	private AppCombo<Location> barangayCombo;

	@Autowired
	private AppCombo<Channel> channelCombo;

	@Autowired
	private AppCombo<CustomerType> typeCombo;

	@Autowired
	private AppCombo<VisitFrequency> visitCombo;

	@Autowired
	private RoutingTable routingTable;

	public CustomerTab() {
		super("Basic Information");
	}

	@Override
	public CustomerTab build() {
		super.build();
		setBindings();
		setListeners();
		return this;
	}

	public AppField<String> nameField() {
		return nameField;
	}

	@Override
	public void refresh() {
		idField.setValue(service.getId());
		nameField.setText(service.getName());
		streetField.setText(service.getStreet());
		provinceCombo.select(service.getProvince());
		cityCombo.select(service.getCity());
		barangayCombo.select(service.getBarangay());
		typeCombo.select(service.getType());
		channelCombo.select(service.getChannel());
		visitCombo.select(service.getVisitFrequency());
		routingTable.items(service.getRouteHistory());
	}

	@Override
	public void save() {
		service.setName(nameField.getText());
		service.setStreet(streetField.getText());
		service.setProvince(provinceCombo.getValue());
		service.setCity(cityCombo.getValue());
		service.setBarangay(barangayCombo.getValue());

		CustomerType type = typeCombo.getValue();
		service.setType(type);
		if (type == CustomerType.OUTLET) {
			service.setChannel(channelCombo.getValue());
			service.setVisitFrequency(visitCombo.getValue());
			service.setRouteHistory(routingTable.getItems());
		}
	}

	@Override
	public void select() {
		super.select();
		if (service.isNew())
			nameField.requestFocus();
	}

	public BooleanBinding showsANewCustomer() {
		return idField.isEmpty();
	}

	public BooleanBinding showsCustomerIsAnOutlet() {
		return showsCustomerIsNotAnOutlet().not();
	}

	public BooleanBinding showsCustomerIsNotAnOutlet() {
		return typeCombo.isNot(CustomerType.OUTLET);
	}

	public BooleanBinding showsNoDesignatedRoutes() {
		return routingTable.isEmpty();
	}

	public BooleanBinding showsNoSelectedCustomerType() {
		return typeCombo.isEmpty();
	}

	private void clearNextControls() {
		channelCombo.clear();
		visitCombo.clear();
		routingTable.getItems().clear();
	}

	private void clearTypeCombo() {
		typeCombo.clear();
	}

	private Node customerBox() {
		idField.readOnly().build(ID);
		nameField.build(TEXT);
		return box.gridpane(idField, label.name("Name"), nameField);
	}

	private void handleError(ErrorHandling control, Exception e) {
		dialog.show(e).addParent(this).start();
		control.handleError();
	}

	private Node parentBox() {
		HBox hbox = box.gridpane(label.name("Parent / Former ID No."), parentIdField.build(ID));
		hbox.setAlignment(Pos.CENTER_RIGHT);
		return hbox;
	}

	private ObservableBooleanValue posted() {
		return nameField.disabled();
	}

	private AppCombo<Location> provinceCombo() throws Exception {
		return provinceCombo.width(180).items(service.listProvinces());
	}

	private void setBarangayComboItems(Location city) {
		try {
			barangayCombo.items(service.listBarangays(city));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setBindings() {
		streetField.disableIf(nameField.isEmpty().or(posted()));
		parentIdField.disableIf(nameField.isEmpty().or(posted()));
		provinceCombo.disableIf(streetField.isEmpty().or(posted()));
		cityCombo.disableIf(provinceCombo.isEmpty().or(posted()));
		barangayCombo.disableIf(cityCombo.isEmpty().or(posted()));
		typeCombo.disableIf(barangayCombo.isEmpty().or(posted()));
		channelCombo.disableIf(typeCombo.isEmpty().or(typeCombo.isNot(CustomerType.OUTLET)).or(posted()));
		visitCombo.disableIf(channelCombo.isEmpty().or(posted()));
		routingTable.disableIf(visitCombo.isEmpty().or(posted()));
	}

	private void setCityComboItems(Location province) {
		try {
			cityCombo.items(service.listCities(province));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {
		nameField.setOnAction(value -> validateName());
		parentIdField.setOnAction(value -> validateParent());
		provinceCombo.setOnAction(value -> clearTypeCombo());
		cityCombo.setOnAction(value -> clearTypeCombo());
		barangayCombo.setOnAction(value -> clearTypeCombo());
		typeCombo.setOnAction(value -> clearNextControls());
		provinceCombo.setOnAction(value -> setCityComboItems(provinceCombo.getValue()));
		cityCombo.setOnAction(value -> setBarangayComboItems(cityCombo.getValue()));
	}

	private VBox tablePane() {
		return box.vbox(label.group("Route Assignment"), routingTable.build());
	}

	private void validateName() {
		try {
			service.setNameIfUnique(nameField.getValue());
		} catch (Exception e) {
			handleError(nameField, e);
		}
	}

	private void validateParent() {
		try {
			service.setParentIfExists(parentIdField.getValue());
			parentDisplay.setValue(service.getParentName());
		} catch (Exception e) {
			parentDisplay.clear();
			handleError(parentIdField, e);
		}
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		try {
			gridPane.getChildren().clear();
			gridPane.add(label.field("ID No."), 0, 0);
			gridPane.add(customerBox(), 1, 0, 4, 1);
			gridPane.add(parentBox(), 6, 0, 2, 1);

			gridPane.add(label.field("Street"), 0, 1);
			gridPane.add(streetField.build(TEXT), 1, 1, 4, 1);
			gridPane.add(parentDisplay.readOnly().build(TEXT), 6, 1, 2, 1);

			gridPane.add(label.field("Province"), 0, 2);
			gridPane.add(provinceCombo(), 1, 2, 2, 1);
			gridPane.add(label.field("City/Town"), 3, 2);
			gridPane.add(cityCombo.width(200), 4, 2, 2, 1);
			gridPane.add(label.field("Barangay"), 6, 2);
			gridPane.add(barangayCombo.width(280), 7, 2);

			gridPane.add(label.field("Type"), 0, 3);
			gridPane.add(typeCombo.items(CustomerType.values()), 1, 3, 2, 1);
			gridPane.add(label.field("Channel"), 3, 3);
			gridPane.add(channelCombo.items(service.listChannels()), 4, 3);
			gridPane.add(label.field("Visit per Month"), 5, 3, 2, 1);
			gridPane.add(visitCombo.items(VisitFrequency.values()), 7, 3);

			return Arrays.asList(gridPane, box.hpane(tablePane()));
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
			return null;
		}
	}
}
