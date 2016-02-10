package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditDetail;
import ph.txtdis.fx.dialog.CreditDialog;

@Scope("prototype")
@Component("creditTable")
public class CreditTable extends AppTable<CreditDetail> {

	@Autowired
	private Column<CreditDetail, Integer> termInDays;

	@Autowired
	private Column<CreditDetail, Integer> gracePeriodInDays;

	@Autowired
	private Column<CreditDetail, BigDecimal> creditLimit;

	@Autowired
	private DecisionNeededTableControls<CreditDetail> decisionNeeded;

	@Autowired
	private CreditDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(termInDays.ofType(INTEGER).build("Term", "termInDays"),
				gracePeriodInDays.ofType(INTEGER).build("Grace\nPeriod", "gracePeriodInDays"),
				creditLimit.ofType(CURRENCY).build("Credit\nLimit", "creditLimit"));
		getColumns().addAll(decisionNeeded.addColumns());
	}

	@Override
	protected void addProperties() {
		setOnEmpty("Complete contact details\nto enable data entry");
		decisionNeeded.addContextMenu(this, dialog);
	}
}
