package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.QUANTITY;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Bom;
import ph.txtdis.dto.Item;
import ph.txtdis.fx.dialog.BomDialog;

@Lazy
@Component("bomTable")
public class BomTable extends AppTable<Bom> {

	@Autowired
	private AppendContextMenu<Bom> append;

	@Autowired
	private BomDialog dialog;

	@Autowired
	private Column<Bom, Item> item;

	@Autowired
	private Column<Bom, BigDecimal> qty;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				item.ofType(OTHERS).width(180).build("Item", "part"), //
				qty.ofType(QUANTITY).build("Quantity", "qty"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
