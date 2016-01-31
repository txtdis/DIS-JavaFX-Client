package ph.txtdis.app;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Item;
import ph.txtdis.fx.AppSelectable;
import ph.txtdis.fx.table.ItemListTable;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("itemListApp")
public class ItemListApp extends AbstractExcelApp<ItemListTable, ItemService, Item> implements AppSelectable<Item> {

	@Override
	public Item getSelection() {
		return table.getItem();
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		table.setItem(null);
		refresh();
		showAndWait();
	}

	@Override
	protected String getHeaderText() {
		return "Item List";
	}

	@Override
	protected String getTitleText() {
		return getHeaderText();
	}
}
