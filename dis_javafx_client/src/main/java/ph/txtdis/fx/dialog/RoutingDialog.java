package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Route;
import ph.txtdis.dto.Routing;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.service.CustomerService;

@Scope("prototype")
@Component("routingDialog")
public class RoutingDialog extends FieldDialog<Routing> {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabeledCombo<Route> routeCombo;

	@Autowired
	private LabeledDatePicker startDatePicker;

	private Routing routing;

	private void createRouteAssignmentUponValidation() {
		try {
			if (startDatePicker.getValue() != null)
				routing = service.createRouteAssignmentUponValidation(routeCombo.getValue(),
						startDatePicker.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private List<Route> getRoutes() {
		try {
			return service.listRoutes();
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	private LabeledDatePicker startDatePicker() {
		startDatePicker.name("Start Date");
		startDatePicker.setOnAction(value -> createRouteAssignmentUponValidation());
		return startDatePicker;
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		routeCombo.name("Route").items(getRoutes()).build();
		return asList(routeCombo, startDatePicker());
	}

	@Override
	protected Routing createEntity() {
		return routing;
	}

	@Override
	protected String headerText() {
		return "Add New Routing";
	}
}
