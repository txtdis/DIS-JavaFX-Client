package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.table.ItemFamilyTable;
import ph.txtdis.service.ItemFamilyService;

@Component("itemFamilyApp")
public class ItemFamilyApp extends AbstractTableApp<ItemFamilyTable, ItemFamilyService, ItemFamily> {
}
