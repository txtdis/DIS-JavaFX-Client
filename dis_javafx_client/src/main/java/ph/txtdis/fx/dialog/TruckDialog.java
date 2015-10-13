package ph.txtdis.fx.dialog;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.service.TruckService;

@Component("truckDialog")
public class TruckDialog extends NameListDialog<Truck, TruckService> {
}
