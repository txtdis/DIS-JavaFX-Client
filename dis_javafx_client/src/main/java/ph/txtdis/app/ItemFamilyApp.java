package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.table.ItemFamilyTable;
import ph.txtdis.service.ItemFamilyService;

@Lazy
@Component("itemFamilyApp")
public class ItemFamilyApp extends AbstractTableApp<ItemFamilyTable, ItemFamilyService, ItemFamily> {

	@Override
	protected String getHeaderText() {
		return "Item Family List";
	}

	@Override
	protected String getTitleText() {
		return "Item Families";
	}
}
