package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditNoteDump;

@Lazy
@Component("creditNoteDumpTable")
public class CreditNoteDumpTable extends AppTable<CreditNoteDump> {

	@Autowired
	private Column<CreditNoteDump, Long> id;

	@Autowired
	private Column<CreditNoteDump, LocalDate> creditDate, paymentDate;

	@Autowired
	private Column<CreditNoteDump, String> description, paymentRemarks, reference, remarks;

	@Autowired
	private Column<CreditNoteDump, BigDecimal> balance, payment, total;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).build("ID No.", "id"), //
				creditDate.ofType(DATE).build("Date", "creditDate"), //
				description.ofType(TEXT).build("Description", "description"), //
				total.ofType(CURRENCY).build("Total", "totalValue"), //
				balance.ofType(CURRENCY).build("Balance", "balanceValue"), //
				remarks.ofType(TEXT).width(480).build("Remarks", "remarks"), //
				paymentDate.ofType(DATE).build("Date", "paymentDate"), //
				reference.ofType(TEXT).width(120).build("Reference", "reference"), //
				payment.ofType(CURRENCY).build("Payment", "paymentValue"), //
				paymentRemarks.ofType(TEXT).width(480).build("Remarks", "paymentRemarks"));
	}
}
