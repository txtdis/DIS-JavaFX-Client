package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditDetail;
import ph.txtdis.fx.dialog.CreditDialog;

@Scope("prototype")
@Component("creditTable")
public class CreditTable extends AppTable<CreditDetail> {

	@Autowired
	private AppendContextMenu<CreditDetail> append;

	@Autowired
	private Column<CreditDetail, Integer> termInDays;

	@Autowired
	private Column<CreditDetail, Integer> gracePeriodInDays;

	@Autowired
	private Column<CreditDetail, BigDecimal> creditLimit;

	@Autowired
	private Column<CreditDetail, LocalDate> startDate;

	@Autowired
	private Column<CreditDetail, String> givenBy;

	@Autowired
	private Column<CreditDetail, ZonedDateTime> givenOn;

	@Autowired
	private Column<CreditDetail, Boolean> approved;

	@Autowired
	private Column<CreditDetail, String> decidedBy;

	@Autowired
	private Column<CreditDetail, ZonedDateTime> decidedOn;

	@Autowired
	private Column<CreditDetail, String> remarks;

	@Autowired
	private CreditDialog creditDialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(termInDays.ofType(INTEGER).build("Term", "termInDays"),
				gracePeriodInDays.ofType(INTEGER).build("Grace\nPeriod", "gracePeriodInDays"),
				creditLimit.ofType(CURRENCY).build("Credit\nLimit", "creditLimit"),
				startDate.ofType(DATE).build("Start\nDate", "startDate"),
				givenBy.ofType(TEXT).width(100).build("Given\nby", "createdBy"),
				givenOn.ofType(TIMESTAMP).build("Given\non", "createdOn"),
				approved.ofType(BOOLEAN).build("OK'd", "approved"),
				decidedBy.ofType(TEXT).width(120).build("Dis/approved\nby", "decidedBy"),
				decidedOn.ofType(TIMESTAMP).build("Dis/approved\non", "decidedOn"),
				remarks.ofType(TEXT).width(320).build("Remarks", "remarks"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, creditDialog);
	}
}
