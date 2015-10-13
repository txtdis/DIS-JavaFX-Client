package ph.txtdis.fx.table;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.fx.dialog.TruckDialog;

@Component("truckTable")
public class TruckTable extends NameListTable<Truck, TruckDialog> {
}
