package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.PaymentHistory;

@Lazy
@Component("paymentHistoryTable")
public class PaymentHistoryTable extends AppTable<PaymentHistory> {

	@Autowired
	private Column<PaymentHistory, Long> id;

	@Autowired
	private Column<PaymentHistory, LocalDate> orderDate;

	@Autowired
	private Column<PaymentHistory, BigDecimal> value;

	@Autowired
	private Column<PaymentHistory, Boolean> isDeposited;

	@Autowired
	private Column<PaymentHistory, BigDecimal> isValid;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).width(120).build("Collection ID", "id"), //
				orderDate.ofType(DATE).build("Date", "paymentDate"), //
				value.ofType(CURRENCY).build("Payment", "value"), //
				isDeposited.ofType(BOOLEAN).build("Deposit", "depositBool"), //
				isValid.ofType(BOOLEAN).build("Valid", "valid"));
	}
}
