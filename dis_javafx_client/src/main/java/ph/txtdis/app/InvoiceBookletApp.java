package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.fx.table.InvoiceBookletTable;
import ph.txtdis.service.InvoiceBookletService;

@Lazy
@Component("invoiceBookleApp")
public class InvoiceBookletApp extends AbstractTableApp<InvoiceBookletTable, InvoiceBookletService, InvoiceBooklet> {

	@Override
	protected String getHeaderText() {
		return "Invoice Booklet List";
	}

	@Override
	protected String getTitleText() {
		return "S/I Booklets";
	}
}
