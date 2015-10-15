package ph.txtdis.fx.table;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.fx.dialog.WarehouseDialog;

@Lazy
@Component("warehouseTable")
public class WarehouseTable extends NameListTable<Warehouse, WarehouseDialog> {
}
