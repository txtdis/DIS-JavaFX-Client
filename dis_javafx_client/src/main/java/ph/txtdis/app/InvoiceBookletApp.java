package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.fx.table.InvoiceBookletTable;
import ph.txtdis.service.InvoiceBookletService;

@Component("invoiceBookleApp")
public class InvoiceBookletApp extends AbstractTableApp<InvoiceBookletTable, InvoiceBookletService, InvoiceBooklet> {
}
