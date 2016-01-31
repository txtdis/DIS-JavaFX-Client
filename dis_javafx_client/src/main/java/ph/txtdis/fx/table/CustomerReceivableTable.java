package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.SalesApp;
import ph.txtdis.dto.CustomerReceivable;

@Lazy
@Component("customerReceivableTable")
public class CustomerReceivableTable extends AppTable<CustomerReceivable> {

	@Autowired
	private SalesApp salesApp;

	@Autowired
	private Column<CustomerReceivable, String> id;

	@Autowired
	private Column<CustomerReceivable, LocalDate> orderDate;

	@Autowired
	private Column<CustomerReceivable, LocalDate> dueDate;

	@Autowired
	private Column<CustomerReceivable, Integer> daysOver;

	@Autowired
	private Column<CustomerReceivable, BigDecimal> totalValue;

	@Autowired
	private Column<CustomerReceivable, BigDecimal> unpaidValue;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.launches(salesApp).ofType(CODE).width(120).build("S/I(D/R) No.", "orderNo"),
				orderDate.launches(salesApp).ofType(DATE).build("Date", "orderDate"),
				dueDate.launches(salesApp).ofType(DATE).build("Due", "dueDate"),
				daysOver.launches(salesApp).ofType(INTEGER).build("Days Over", "daysOverCount"),
				totalValue.launches(salesApp).ofType(CURRENCY).build("Total Amount", "totalValue"),
				unpaidValue.launches(salesApp).ofType(CURRENCY).build("Unpaid Balance", "unpaidValue"));
	}
}
