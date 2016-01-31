package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.StockTakeDetail;
import ph.txtdis.fx.dialog.StockTakeDialog;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Lazy
@Component("stockTakeTable")
public class StockTakeTable extends AppTable<StockTakeDetail> {

	@Autowired
	private AppendContextMenu<StockTakeDetail> append;

	@Autowired
	private DeleteContextMenu<StockTakeDetail> subtract;

	@Autowired
	private Column<StockTakeDetail, Long> id;

	@Autowired
	private Column<StockTakeDetail, String> name;

	@Autowired
	private Column<StockTakeDetail, UomType> uom;

	@Autowired
	private Column<StockTakeDetail, QualityType> quality;

	@Autowired
	private Column<StockTakeDetail, BigDecimal> quantity;

	@Autowired
	private StockTakeDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).build("ID No.", "id"), //
				name.ofType(TEXT).width(180).build("Name", "name"), //
				uom.ofType(ENUM).build("UOM", "uom"), //
				quality.ofType(ENUM).build("Quality", "quality"), //
				quantity.ofType(QUANTITY).build("Quantity", "qty")); //
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
		subtract.addMenu(this);
	}
}
