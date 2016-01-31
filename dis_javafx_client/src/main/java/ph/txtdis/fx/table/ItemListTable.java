package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Item;
import ph.txtdis.type.Type;

@Lazy
@Component("itemListTable")
public class ItemListTable extends AppTable<Item> {

	@Autowired
	private Column<Item, Long> id;

	@Autowired
	private Column<Item, String> name;

	@Autowired
	private Column<Item, String> description;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).build("ID No.", "id"), //
				name.ofType(Type.TEXT).width(240).build("Name", "name"), //
				description.ofType(TEXT).width(480).build("Description", "description"));
	}
}
