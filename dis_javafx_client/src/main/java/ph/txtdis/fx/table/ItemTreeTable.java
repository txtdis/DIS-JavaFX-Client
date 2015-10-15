package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.fx.dialog.ItemTreeDialog;

@Lazy
@Component("itemTreeTable")
public class ItemTreeTable extends AppTable<ItemTree> {

	@Autowired
	private AppendableTableProperty<ItemTree> append;

	@Autowired
	private Column<ItemTree, ItemFamily> family;

	@Autowired
	private Column<ItemTree, ItemFamily> parent;

	@Autowired
	private Column<ItemTree, String> createdBy;

	@Autowired
	private Column<ItemTree, ZonedDateTime> createdOn;

	@Autowired
	private ItemTreeDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(family.ofType(TEXT).width(180).build("Family", "family"),
				parent.ofType(TEXT).width(180).build("Parent", "parent"),
				createdBy.ofType(TEXT).width(120).build("Issued by", "createdBy"),
				createdOn.ofType(TIMESTAMP).build("Issued on", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
	}
}
