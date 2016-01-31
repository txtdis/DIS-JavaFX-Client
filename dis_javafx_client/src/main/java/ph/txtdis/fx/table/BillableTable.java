package ph.txtdis.fx.table;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.control.TableColumn;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.fx.dialog.BillableDialog;
import ph.txtdis.fx.dialog.Inputted;
import ph.txtdis.fx.dialog.PurchasingDialog;
import ph.txtdis.fx.dialog.ReceivingDialog;
import ph.txtdis.service.BillableService;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("billableTable")
public class BillableTable extends AppTable<BillableDetail> {

	@Autowired
	private AppendContextMenu<BillableDetail> append;

	@Autowired
	private DeleteContextMenu<BillableDetail> subtract;

	@Autowired
	private Column<BillableDetail, Long> id;

	@Autowired
	private Column<BillableDetail, String> name;

	@Autowired
	private Column<BillableDetail, UomType> uom;

	@Autowired
	private Column<BillableDetail, QualityType> quality;

	@Autowired
	private Column<BillableDetail, BigDecimal> price;

	@Autowired
	private Column<BillableDetail, BigDecimal> quantity;

	@Autowired
	private Column<BillableDetail, BigDecimal> subtotal;

	@Autowired
	private Column<BillableDetail, Integer> onPurchaseDaysLevel;

	@Autowired
	private Column<BillableDetail, Integer> onReceiptDaysLevel;

	@Autowired
	private BillableDialog billableDialog;

	@Autowired
	private PurchasingDialog purchasingDialog;

	@Autowired
	private ReceivingDialog receivingDialog;

	@Autowired
	private BillableService service;

	public void initializeColumns() {
		id.ofType(ID).build("ID No.", "id");
		name.ofType(TEXT).width(180).build("Name", "itemName");
		uom.ofType(ENUM).build("UOM", "uom");
		quality.ofType(ENUM).build("Quality", "quality");
		price.ofType(CURRENCY).build("Price", "priceValue");
		quantity.ofType(QUANTITY).build("Quantity", qty());
		subtotal.ofType(CURRENCY).build("Subtotal", "subtotalValue");
		onPurchaseDaysLevel.ofType(INTEGER).width(120).build("On-Purchase Days Lavel", "onPurchaseDaysLevel");
		onReceiptDaysLevel.ofType(INTEGER).width(120).build("On-Receipt Days Lavel", "onReceiptDaysLevel");
	}

	public String qty() {
		if (service.isABooking())
			return "initialQty";
		return service.isAReceiving() ? "returnedQty" : "qty";
	}

	private List<TableColumn<BillableDetail, ?>> columns() {
		List<TableColumn<BillableDetail, ?>> l = new ArrayList<>(asList(id, name, uom, quality));
		if (service.isAReceiving())
			l.add(quantity);
		else if (service.isAPurchaseOrder())
			l.addAll(asList(quantity, onPurchaseDaysLevel, onReceiptDaysLevel));
		else
			l.addAll(asList(price, quantity, subtotal));
		return l;
	}

	private Inputted<BillableDetail> dialog() {
		if (service.isAPurchaseOrder())
			return purchasingDialog;
		return service.isAReceiving() ? receivingDialog : billableDialog;
	}

	@Override
	protected void addColumns() {
		initializeColumns();
		getColumns().setAll(columns());
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog(), service);
		subtract.addMenu(this);
	}
}
