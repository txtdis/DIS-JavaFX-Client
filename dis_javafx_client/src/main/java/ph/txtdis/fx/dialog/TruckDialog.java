package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.service.TruckService;

@Lazy
@Component("truckDialog")
public class TruckDialog extends NameListDialog<Truck, TruckService> {
}
