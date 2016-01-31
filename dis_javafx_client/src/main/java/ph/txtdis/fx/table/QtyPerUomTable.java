package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.QUANTITY;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.fx.dialog.QtyPerUomDialog;
import ph.txtdis.type.UomType;

@Lazy
@Component("qtyPerUomTable")
public class QtyPerUomTable extends AppTable<QtyPerUom> {

	@Autowired
	private AppendContextMenu<QtyPerUom> append;

	@Autowired
	private Column<QtyPerUom, UomType> uom;

	@Autowired
	private Column<QtyPerUom, BigDecimal> qty;

	@Autowired
	private Column<QtyPerUom, Boolean> isPurchased;

	@Autowired
	private Column<QtyPerUom, Boolean> isSold;

	@Autowired
	private Column<QtyPerUom, Boolean> isReported;

	@Autowired
	private QtyPerUomDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				uom.ofType(ENUM).build("UOM", "uom"), //
				qty.ofType(QUANTITY).build("Quantity", "qty"), //
				isPurchased.ofType(BOOLEAN).width(120).build("Purchased", "purchased"), //
				isSold.ofType(BOOLEAN).width(120).build("Sold", "sold"), //
				isReported.ofType(BOOLEAN).width(120).build("Reported", "reported"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
