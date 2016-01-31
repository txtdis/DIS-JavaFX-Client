package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.PaymentHistory;
import ph.txtdis.fx.table.PaymentHistoryTable;
import ph.txtdis.service.RemittanceService;

@Lazy
@Component("paymentHistoryApp")
public class PaymentHistoryApp extends AbstractExcelApp<PaymentHistoryTable, RemittanceService, PaymentHistory> {

	public Long getSelectedId() {
		PaymentHistory ph = table.getItem();
		return ph == null ? null : ph.getId();
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		refresh();
		showAndWait();
	}

	@Override
	protected String getHeaderText() {
		return service.getHeaderText() + " List";
	}

	@Override
	protected String getTitleText() {
		return service.getHeaderText() + " History";
	}
}
