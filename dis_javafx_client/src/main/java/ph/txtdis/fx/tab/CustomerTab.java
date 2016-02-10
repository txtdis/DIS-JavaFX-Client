package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;
import static javafx.beans.binding.Bindings.when;
import static javafx.geometry.Pos.CENTER_RIGHT;
import static ph.txtdis.type.PartnerType.CUSTOMER;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.WeeklyVisit;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.table.RoutingTable;
import ph.txtdis.fx.table.VisitScheduleTable;
import ph.txtdis.service.CustomerService;
import ph.txtdis.type.PartnerType;
import ph.txtdis.type.VisitFrequency;

@Scope("prototype")
@Component("customerTab")
public class CustomerTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<Long> idField, parentIdField;

	@Autowired
	private AppField<String> nameField, parentDisplay, streetField;

	@Autowired
	private AppCombo<Location> provinceCombo, cityCombo, barangayCombo;

	@Autowired
	private AppCombo<Channel> channelCombo;

	@Autowired
	private AppCombo<PartnerType> typeCombo;

	@Autowired
	private AppCombo<VisitFrequency> visitCombo;

	@Autowired
	private RoutingTable routingTable;

	@Autowired
	private VisitScheduleTable scheduleTable;

	public CustomerTab() {
		super("Basic Information");
	}

	public void disableNameFieldIf(ObservableBooleanValue b) {
		nameField.disableIf(b);
	}

	public BooleanBinding hasIncompleteData() {
		return typeCombo.isEmpty().or(showsPartnerAsACustomer().and(routingTable.isEmpty()));
	}

	@Override
	public void refresh() {
		idField.setValue(service.getId());
		nameField.setText(customer().getName());
		parentIdField.setValue(service.getParentId());
		parentDisplay.setValue(service.getParentName());
		streetField.setText(customer().getStreet());
		provinceCombo.select(customer().getProvince());
		cityCombo.select(customer().getCity());
		barangayCombo.select(customer().getBarangay());
		typeCombo.items(service.getTypes());
		channelCombo.items(service.getChannels());
		visitCombo.select(customer().getVisitFrequency());
		routingTable.items(customer().getRouteHistory());
		setScheduleTableItem();
	}

	@Override
	public void save() {
		customer().setName(nameField.getText());
		customer().setStreet(streetField.getText());
		customer().setProvince(provinceCombo.getValue());
		customer().setCity(cityCombo.getValue());
		customer().setBarangay(barangayCombo.getValue());

		PartnerType type = typeCombo.getValue();
		service.setType(type);
		if (type == CUSTOMER) {
			customer().setChannel(channelCombo.getValue());
			customer().setVisitFrequency(visitCombo.getValue());
			customer().setRouteHistory(routingTable.getItems());
			customer().setVisitSchedule(scheduleTable.getItems());
		}
	}

	@Override
	public void select() {
		super.select();
		if (service.isNew())
			nameField.requestFocus();
	}

	public BooleanBinding showsPartnerAsACustomer() {
		return typeCombo.is(CUSTOMER);
	}

	private void clearNextControls() {
		if (!service.isNew())
			return;
		channelCombo.clear();
		visitCombo.clear();
		routingTable.getItems().clear();
		scheduleTable.getItems().clear();
	}

	private void clearTypeCombo() {
		if (service.isNew())
			typeCombo.clear();
	}

	private Customer customer() {
		return service.get();
	}

	private Node customerBox() {
		idField.readOnly().build(ID);
		nameField.build(TEXT);
		return box.forIdName(idField, label.name("Name"), nameField);
	}

	private BooleanBinding notVisitedChannel() {
		return channelCombo.are(service.listVisitedChannels()).not();
	}

	private Node parentBox() {
		HBox h = box.forIdName(label.name("Parent / Former ID No."), parentIdField.build(ID));
		h.setAlignment(CENTER_RIGHT);
		return h;
	}

	private VBox routeTablePane() {
		return box.forVerticals(label.group("Route Assignment"), routingTable.build());
	}

	private VBox scheduleTablePane() {
		return box.forVerticals(label.group("Visit Schedule"), scheduleTable.build());
	}

	private void setBarangayComboItems() {
		Location c = cityCombo.getValue();
		List<Location> l = service.listBarangays(c);
		barangayCombo.items(l);
	}

	private void setBarangayComboItemsAfterClearingTypeCombo() {
		if (service.isNew())
			clearTypeCombo();
		setBarangayComboItems();
	}

	private void setCityComboItems() {
		Location p = provinceCombo.getValue();
		List<Location> l = service.listCities(p);
		cityCombo.items(l);
	}

	private void setCityComboItemsAfterClearingTypeCombo() {
		if (service.isNew())
			clearTypeCombo();
		setCityComboItems();
	}

	private void setScheduleTableItem() {
		Channel c = channelCombo.getValue();
		List<WeeklyVisit> vs = service.getVisitSchedule(c);
		scheduleTable.items(vs);
	}

	private HBox tableBox() {
		return box.forHorizontalPane(routeTablePane(), scheduleTablePane());
	}

	private void validateName() {
		try {
			service.setNameIfUnique(nameField.getValue());
		} catch (Exception e) {
			handleError(nameField, e);
		}
	}

	private void validateParent() {
		if (parentIdField.getValue() != 0L)
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
		gridPane.getChildren().clear();
		gridPane.add(label.field("ID No."), 0, 0);
		gridPane.add(customerBox(), 1, 0, 4, 1);
		gridPane.add(parentBox(), 6, 0, 2, 1);

		gridPane.add(label.field("Street"), 0, 1);
		gridPane.add(streetField.build(TEXT), 1, 1, 4, 1);
		gridPane.add(parentDisplay.readOnly().build(TEXT), 6, 1, 2, 1);

		gridPane.add(label.field("Province"), 0, 2);
		gridPane.add(provinceCombo.width(180).items(service.listProvinces()), 1, 2, 2, 1);
		gridPane.add(label.field("City/Town"), 3, 2);
		gridPane.add(cityCombo.width(200), 4, 2, 2, 1);
		gridPane.add(label.field("Barangay"), 6, 2);
		gridPane.add(barangayCombo.width(280), 7, 2);

		gridPane.add(label.field("Type"), 0, 3);
		gridPane.add(typeCombo, 1, 3, 2, 1);
		gridPane.add(label.field("Channel"), 3, 3);
		gridPane.add(channelCombo, 4, 3);
		gridPane.add(label.field("Visit per Month"), 5, 3, 2, 1);
		gridPane.add(visitCombo.items(VisitFrequency.values()), 7, 3);

		return asList(gridPane, tableBox());
	}

	@Override
	protected void setBindings() {
		streetField.disableIf(nameField.isEmpty());
		parentIdField.disableIf(nameField.isEmpty());
		provinceCombo.disableIf(streetField.isEmpty());
		cityCombo.disableIf(provinceCombo.isEmpty());
		barangayCombo.disableIf(cityCombo.isEmpty());
		typeCombo.disableIf(barangayCombo.isEmpty());
		channelCombo.disableIf(typeCombo.isEmpty()//
				.or(typeCombo.isNot(CUSTOMER)));
		visitCombo.disableIf(channelCombo.isEmpty()//
				.or(notVisitedChannel()));
		routingTable.disableIf(when(notVisitedChannel())//
				.then(channelCombo.isEmpty())//
				.otherwise(visitCombo.isEmpty()));
		scheduleTable.disableIf(routingTable.isEmpty()//
				.or(channelCombo.disabledProperty())//
				.or(notVisitedChannel()));
	}

	@Override
	protected void setListeners() {
		nameField.setOnAction(e -> validateName());
		parentIdField.setOnAction(e -> validateParent());
		provinceCombo.setOnAction(e -> setCityComboItemsAfterClearingTypeCombo());
		cityCombo.setOnAction(e -> setBarangayComboItemsAfterClearingTypeCombo());
		barangayCombo.setOnAction(e -> clearTypeCombo());
		typeCombo.setOnAction(e -> clearNextControls());
		channelCombo.setOnAction(e -> setScheduleTableItem());
	}
}
