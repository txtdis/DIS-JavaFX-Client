package ph.txtdis.fx.table;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.fx.dialog.WarehouseDialog;

@Component("warehouseTable")
public class WarehouseTable extends NameListTable<Warehouse, WarehouseDialog> {
}