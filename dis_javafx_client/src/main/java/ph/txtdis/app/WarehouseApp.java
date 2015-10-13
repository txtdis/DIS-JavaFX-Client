package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.fx.table.WarehouseTable;
import ph.txtdis.service.WarehouseService;

@Component("warehouseApp")
public class WarehouseApp extends AbstractTableApp<WarehouseTable, WarehouseService, Warehouse> {
}
