package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.service.WarehouseService;

@Lazy
@Component("warehouseDialog")
public class WarehouseDialog extends NameListDialog<Warehouse, WarehouseService> {
}