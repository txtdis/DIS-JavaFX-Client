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

import ph.txtdis.app.SalesApp;
import ph.txtdis.dto.PaymentDetail;
import ph.txtdis.fx.dialog.PaymentDialog;
import ph.txtdis.service.RemittanceService;

@Lazy
@Component("paymentTable")
public class PaymentTable extends AppTable<PaymentDetail> {

	@Autowired
	private SalesApp salesApp;

	@Autowired
	private AppendContextMenu<PaymentDetail> append;

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

	@Autowired
	private PaymentDialog dialog;

	@Autowired
	private RemittanceService service;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				orderNo.launches(salesApp).ofType(CODE).width(120).build("S/I(D/R) No.", "orderNo"), //
				customer.launches(salesApp).ofType(TEXT).width(320).build("Customer", "customerName"), //
				dueDate.launches(salesApp).ofType(DATE).build("Due Date", "dueDate"), //
				total.launches(salesApp).ofType(CURRENCY).build("Amount Due", "totalDueValue"), //
				payment.launches(salesApp).ofType(CURRENCY).build("Payment", "paymentValue"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog, service);
	}
}
