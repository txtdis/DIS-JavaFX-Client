package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DECIMAL;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.SoldDetail;
import ph.txtdis.fx.dialog.InvoiceDialog;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("invoiceTable")
public class InvoiceTable extends AppTable<SoldDetail> {

	@Autowired
	private AppendableTableProperty<SoldDetail> append;

	@Autowired
	private ReducableTableProperty<SoldDetail> subtract;

	@Autowired
	private Column<SoldDetail, Long> id;

	@Autowired
	private Column<SoldDetail, String> name;

	@Autowired
	private Column<SoldDetail, UomType> uom;

	@Autowired
	private Column<SoldDetail, QualityType> quality;

	@Autowired
	private Column<SoldDetail, BigDecimal> price;

	@Autowired
	private Column<SoldDetail, BigDecimal> quantity;

	@Autowired
	private Column<SoldDetail, BigDecimal> subtotal;

	@Autowired
	private InvoiceDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(id.ofType(ID).build("ID No.", "itemId"),
				name.ofType(TEXT).width(180).build("Name", "itemName"), uom.ofType(ENUM).build("UOM", "uom"),
				quality.ofType(ENUM).build("Quality", "quality"), price.ofType(CURRENCY).build("Price", "priceValue"),
				quantity.ofType(DECIMAL).build("Quantity", "qty"),
				subtotal.ofType(CURRENCY).build("Subtotal", "subtotalValue"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
		subtract.addProperties(this);
	}
}
