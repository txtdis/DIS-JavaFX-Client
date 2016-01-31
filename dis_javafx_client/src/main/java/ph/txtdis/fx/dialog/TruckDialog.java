package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.service.TruckService;

@Scope("prototype")
@Component("truckDialog")
public class TruckDialog extends NameListDialog<Truck, TruckService> {

	@Override
	protected String headerText() {
		return "Add New Truck";
	}
}
