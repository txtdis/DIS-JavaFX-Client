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

import ph.txtdis.dto.AbstractSoldOrder;
import ph.txtdis.dto.SoldOrderDetail;
import ph.txtdis.fx.dialog.SoldOrderDialog;
import ph.txtdis.service.SoldService;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("soldOrderTable")
public class SoldOrderTable extends AppTable<SoldOrderDetail> {

	@Autowired
	private AppendContextMenu<SoldOrderDetail> append;

	@Autowired
	private DeleteContextMenu<SoldOrderDetail> subtract;

	@Autowired
	private Column<SoldOrderDetail, Long> id;

	@Autowired
	private Column<SoldOrderDetail, String> name;

	@Autowired
	private Column<SoldOrderDetail, UomType> uom;

	@Autowired
	private Column<SoldOrderDetail, QualityType> quality;

	@Autowired
	private Column<SoldOrderDetail, BigDecimal> price;

	@Autowired
	private Column<SoldOrderDetail, BigDecimal> quantity;

	@Autowired
	private Column<SoldOrderDetail, BigDecimal> subtotal;

	@Autowired
	private SoldOrderDialog dialog;

	private SoldService<? extends AbstractSoldOrder<Long>, Long> service;

	public SoldOrderTable addService(SoldService<? extends AbstractSoldOrder<Long>, Long> service) {
		this.service = service;
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
	// @formatter:off
		getColumns().setAll(
			id.ofType(ID).build("ID No.", "itemId"),
			name.ofType(TEXT).width(180).build("Name", "itemName"),
			uom.ofType(ENUM).build("UOM", "uom"),
			quality.ofType(ENUM).build("Quality", "quality"),
			price.ofType(CURRENCY).build("Price", "priceValue"),
			quantity.ofType(DECIMAL).build("Quantity", "qty"),
			subtotal.ofType(CURRENCY).build("Subtotal", "subtotalValue"));
	// @formatter:on
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog.service(service));
		subtract.addMenu(this);
	}
}
