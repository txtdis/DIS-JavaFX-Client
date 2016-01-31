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

import ph.txtdis.dto.CreditNote;

@Lazy
@Component("creditNoteListTable")
public class CreditNoteListTable extends AppTable<CreditNote> {

	@Autowired
	private Column<CreditNote, Long> id;

	@Autowired
	private Column<CreditNote, LocalDate> creditDate;

	@Autowired
	private Column<CreditNote, String> description;

	@Autowired
	private Column<CreditNote, BigDecimal> balanceValue, totalValue;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).build("ID No.", "id"), //
				creditDate.ofType(DATE).build("Date", "creditDate"), //
				description.ofType(TEXT).build("Description", "description"), //
				totalValue.ofType(CURRENCY).build("Total", "totalValue"), //
				balanceValue.ofType(CURRENCY).build("Balance", "balanceValue"));
	}
}
