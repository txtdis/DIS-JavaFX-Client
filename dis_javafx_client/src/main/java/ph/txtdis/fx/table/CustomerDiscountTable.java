package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.PERCENT;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.dialog.CustomerDiscountDialog;

@Lazy
@Component("customerDiscountTable")
public class CustomerDiscountTable extends AppTable<CustomerDiscount> {

	@Autowired
	private AppendableTableProperty<CustomerDiscount> append;

	@Autowired
	private Column<CustomerDiscount, Integer> level;

	@Autowired
	private Column<CustomerDiscount, BigDecimal> percent;

	@Autowired
	private Column<CustomerDiscount, ItemFamily> familyLimit;

	@Autowired
	private Column<CustomerDiscount, LocalDate> startDate;

	@Autowired
	private Column<CustomerDiscount, String> givenBy;

	@Autowired
	private Column<CustomerDiscount, ZonedDateTime> givenOn;

	@Autowired
	private CustomerDiscountDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(level.ofType(INTEGER).width(100).build("Level", "level"),
				percent.ofType(PERCENT).build("Discount", "percent"),
				familyLimit.ofType(OTHERS).build("Limited\nto", "familyLimit"),
				startDate.ofType(DATE).build("Start\nDate", "startDate"),
				givenBy.ofType(TEXT).width(100).build("Given\nby", "createdBy"),
				givenOn.ofType(TIMESTAMP).build("Given\non", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
	}
}
