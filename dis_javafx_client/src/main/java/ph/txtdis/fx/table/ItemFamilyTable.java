package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.dialog.ItemFamilyDialog;
import ph.txtdis.type.ItemTier;

@Component("itemFamilyTable")
public class ItemFamilyTable extends NameListTable<ItemFamily, ItemFamilyDialog> {

	@Autowired
	private Column<ItemFamily, ItemTier> tier;

	@Override
	protected void addColumns() {
		super.addColumns();
		getColumns().add(2, tier.ofType(TEXT).width(120).build("Tier", "tier"));
	}
}
