package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemTree;
import ph.txtdis.fx.table.ItemTreeTable;
import ph.txtdis.service.ItemTreeService;

@Lazy
@Component("itemTreeApp")
public class ItemTreeApp extends AbstractTableApp<ItemTreeTable, ItemTreeService, ItemTree> {

	@Override
	protected String getHeaderText() {
		return "Item Tree List";
	}

	@Override
	protected String getTitleText() {
		return "Item Trees";
	}
}
