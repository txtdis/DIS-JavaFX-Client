package ph.txtdis.fx.dialog;

import static ph.txtdis.type.DeliveryType.values;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Route;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.service.RouteService;
import ph.txtdis.type.DeliveryType;

@Scope("prototype")
@Component("routeDialog")
public class RouteDialog extends NameListDialog<Route, RouteService> {

	@Autowired
	private LabeledCombo<DeliveryType> typeCombo;

	@Override
	protected List<InputNode<?>> addNodes() {
		List<InputNode<?>> l = new ArrayList<>(super.addNodes());
		l.add(typeCombo.name("Delivery Type").items(values()).build());
		return l;
	}

	@Override
	protected Route createEntity() {
		try {
			return service.save(nameField.getValue(), typeCombo.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected String headerText() {
		return "Add New Route";
	}
}
