package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.ItemApp;
import ph.txtdis.dto.Inventory;

@Lazy
@Component("inventoryTable")
public class InventoryTable extends AppTable<Inventory> {

	@Autowired
	private ItemApp app;

	@Autowired
	private Column<Inventory, Long> id;

	@Autowired
	private Column<Inventory, String> item;

	@Autowired
	private Column<Inventory, BigDecimal> goodQty;

	@Autowired
	private Column<Inventory, BigDecimal> badQty;

	@Autowired
	private Column<Inventory, Integer> daysLevel;

	@Autowired
	private Column<Inventory, BigDecimal> value;

	@Autowired
	private Column<Inventory, BigDecimal> obsolescenceValue;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.launches(app).ofType(ID).build("ID", "id"), //
				item.launches(app).ofType(TEXT).width(180).build("Item", "item"), //
				goodQty.launches(app).ofType(QUANTITY).build("Good Quantity", "goodQty"), //
				badQty.launches(app).ofType(QUANTITY).build("Bad Quantity", "badQty"), //
				daysLevel.launches(app).ofType(INTEGER).width(80).build("Days Level", "daysLevel"), //
				value.launches(app).ofType(CURRENCY).build("Value", "value"), //
				obsolescenceValue.launches(app).ofType(CURRENCY).build("Allowance for Obsolescence",
						"obsolesenceValue"));
	}
}
