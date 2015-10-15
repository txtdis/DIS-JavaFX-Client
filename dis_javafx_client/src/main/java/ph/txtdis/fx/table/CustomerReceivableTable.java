package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.InvoiceApp;
import ph.txtdis.dto.CustomerReceivable;

@Lazy
@Component("customerReceivableTable")
public class CustomerReceivableTable extends AppTable<CustomerReceivable> {

	@Autowired
	private InvoiceApp invoiceApp;

	@Autowired
	private Column<CustomerReceivable, Long> id;

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
		getColumns().setAll(id.launches(invoiceApp).ofType(ID).build("S/I(D/R) No.", "id"),
				orderDate.launches(invoiceApp).ofType(DATE).build("Date", "orderDate"),
				dueDate.launches(invoiceApp).ofType(DATE).build("Due", "dueDate"),
				daysOver.launches(invoiceApp).ofType(INTEGER).build("Days Over", "daysOverCount"),
				totalValue.launches(invoiceApp).ofType(CURRENCY).build("Total Amount", "totalValue"),
				unpaidValue.launches(invoiceApp).ofType(CURRENCY).build("Unpaid Balance", "unpaidValue"));
	}
}
