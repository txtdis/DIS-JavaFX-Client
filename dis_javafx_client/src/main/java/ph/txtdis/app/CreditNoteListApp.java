package ph.txtdis.app;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditNote;
import ph.txtdis.fx.table.CreditNoteListTable;
import ph.txtdis.service.CreditNoteService;

@Lazy
@Component("creditNoteListApp")
public class CreditNoteListApp extends AbstractExcelApp<CreditNoteListTable, CreditNoteService, CreditNote> {

	public CreditNote getSelection() {
		return table.getItem();
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		table.setItem(null);
		refresh();
		showAndWait();
	}

	@Override
	protected String getHeaderText() {
		return service.getPartiallyPaidHeaderText();
	}

	@Override
	protected String getTitleText() {
		return getHeaderText();
	}

	@Override
	protected void saveAsExcel(CreditNoteListTable t) throws IOException {
		service.saveListAsExcel(t);
	}
}
