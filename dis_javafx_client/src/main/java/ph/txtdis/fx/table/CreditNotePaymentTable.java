package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditNotePayment;
import ph.txtdis.fx.dialog.CreditNotePaymentDialog;

@Lazy
@Component("creditNotePaymentTable")
public class CreditNotePaymentTable extends AppTable<CreditNotePayment> {

	@Autowired
	private AppendContextMenu<CreditNotePayment> append;

	@Autowired
	private Column<CreditNotePayment, LocalDate> paymentDate;

	@Autowired
	private Column<CreditNotePayment, String> referenceId, remarks;

	@Autowired
	private Column<CreditNotePayment, BigDecimal> payment;

	@Autowired
	private CreditNotePaymentDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				paymentDate.ofType(DATE).build("Date", "paymentDate"), //
				referenceId.ofType(TEXT).width(120).build("Reference", "reference"), //
				payment.ofType(CURRENCY).build("Payment", "paymentValue"), //
				remarks.ofType(TEXT).width(360).build("Remarks", "paymentRemarks"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
