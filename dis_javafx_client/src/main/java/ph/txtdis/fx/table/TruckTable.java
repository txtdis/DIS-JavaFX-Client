package ph.txtdis.fx.table;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.fx.dialog.TruckDialog;

@Lazy
@Component("truckTable")
public class TruckTable extends NameListTable<Truck, TruckDialog> {
}
