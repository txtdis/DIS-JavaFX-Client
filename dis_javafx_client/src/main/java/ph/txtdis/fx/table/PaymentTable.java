package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.InvoiceApp;
import ph.txtdis.dto.PaymentDetail;

@Lazy
@Component("remittanceTable")
public class PaymentTable extends AppTable<PaymentDetail> {

	@Autowired
	private InvoiceApp invoiceApp;

	@Autowired
	private Column<PaymentDetail, String> orderNo;

	@Autowired
	private Column<PaymentDetail, String> customer;

	@Autowired
	private Column<PaymentDetail, LocalDate> dueDate;

	@Autowired
	private Column<PaymentDetail, BigDecimal> total;

	@Autowired
	private Column<PaymentDetail, BigDecimal> payment;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(orderNo.launches(invoiceApp).ofType(CODE).width(120).build("S/I(D/R) No.", "orderNo"),
				customer.launches(invoiceApp).ofType(TEXT).build("Customer", "customerName"),
				dueDate.launches(invoiceApp).ofType(DATE).build("Due Date", "dueDate"),
				total.launches(invoiceApp).ofType(CURRENCY).build("Amount Due", "totalDueValue"),
				payment.launches(invoiceApp).ofType(CURRENCY).build("Payment", "paymentValue"));
	}
}
