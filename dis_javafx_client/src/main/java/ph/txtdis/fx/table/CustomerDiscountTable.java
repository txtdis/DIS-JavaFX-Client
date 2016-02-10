package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.PERCENT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.dialog.CustomerDiscountDialog;

@Scope("prototype")
@Component("customerDiscountTable")
public class CustomerDiscountTable extends AppTable<CustomerDiscount> {

	@Autowired
	private Column<CustomerDiscount, Integer> level;

	@Autowired
	private Column<CustomerDiscount, BigDecimal> percent;

	@Autowired
	private Column<CustomerDiscount, ItemFamily> familyLimit;

	@Autowired
	private DecisionNeededTableControls<CustomerDiscount> decisionNeeded;

	@Autowired
	private CustomerDiscountDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(level.ofType(INTEGER).width(60).build("Level", "level"),
				percent.ofType(PERCENT).build("Discount", "percent"),
				familyLimit.ofType(OTHERS).build("Limited\nto", "familyLimit"));
		getColumns().addAll(decisionNeeded.addColumns());
	}

	@Override
	protected void addProperties() {
		decisionNeeded.addContextMenu(this, dialog);
	}
}
