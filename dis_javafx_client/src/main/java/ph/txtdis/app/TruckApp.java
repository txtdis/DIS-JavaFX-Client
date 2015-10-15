package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Truck;
import ph.txtdis.fx.table.TruckTable;
import ph.txtdis.service.TruckService;

@Lazy
@Component("truckApp")
public class TruckApp extends AbstractTableApp<TruckTable, TruckService, Truck> {
}
