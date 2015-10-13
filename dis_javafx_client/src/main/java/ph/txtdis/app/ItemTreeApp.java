package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemTree;
import ph.txtdis.fx.table.ItemTreeTable;
import ph.txtdis.service.ItemTreeService;

@Component("itemTreeApp")
public class ItemTreeApp extends AbstractTableApp<ItemTreeTable, ItemTreeService, ItemTree> {
}
