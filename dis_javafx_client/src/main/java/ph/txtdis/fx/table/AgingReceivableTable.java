package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.AccountApp;
import ph.txtdis.app.CustomerApp;
import ph.txtdis.app.CustomerReceivableApp;
import ph.txtdis.dto.AgingReceivable;
import ph.txtdis.service.AgingReceivableService;

@Lazy
@Component("agingReceivableTable")
public class AgingReceivableTable extends AppTable<AgingReceivable> {

	@Autowired
	private AccountApp accountApp;

	@Autowired
	private AgingReceivableService service;

	@Autowired
	private Column<AgingReceivable, String> seller;

	@Autowired
	private Column<AgingReceivable, String> customer;

	@Autowired
	private Column<AgingReceivable, BigDecimal> current;

	@Autowired
	private Column<AgingReceivable, BigDecimal> oneToSeven;

	@Autowired
	private Column<AgingReceivable, BigDecimal> eightToFifteen;

	@Autowired
	private Column<AgingReceivable, BigDecimal> sixteenToThirty;

	@Autowired
	private Column<AgingReceivable, BigDecimal> moreThanThirty;

	@Autowired
	private Column<AgingReceivable, BigDecimal> aging;

	@Autowired
	private Column<AgingReceivable, BigDecimal> total;

	@Autowired
	private CustomerApp customerApp;

	@Autowired
	private CustomerReceivableApp customerReceivableApp;

	@Autowired
	private SellerFilterContextMenu<AgingReceivable> menu;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(seller.launches(accountApp).ofType(TEXT).width(100).build("Seller", "seller"),
				customer.launches(customerApp).ofType(TEXT).width(240).build("Customer", "customer"),
				current.launches(customerReceivableApp).ofType(CURRENCY).build("Current", "currentValue"),
				oneToSeven.launches(customerReceivableApp).ofType(CURRENCY).build("1-7", "oneToSevenValue"),
				eightToFifteen.launches(customerReceivableApp).ofType(CURRENCY).build("8-15", "eightToFifteenValue"),
				sixteenToThirty.launches(customerReceivableApp).ofType(CURRENCY).build("16-30", "sixteenToThirtyValue"),
				moreThanThirty.launches(customerReceivableApp).ofType(CURRENCY).build(">30", "greaterThanThirtyValue"),
				total.launches(customerReceivableApp).ofType(CURRENCY).build("All Overdue", "agingValue"),
				aging.launches(customerReceivableApp).ofType(CURRENCY).build("All Aging", "totalValue"));
	}

	@Override
	protected void addProperties() {
		menu.setMenu(service, this);
	}
}