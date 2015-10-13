package ph.txtdis.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.table.AppTable;
import ph.txtdis.service.Excel;

public abstract class AbstractExcelApp<AT extends AppTable<T>, AS extends Excel<?>, T> extends AbstractTableApp<AT, AS, T> {

	@Autowired
	private AppButton mailButton;

	@Autowired
	private AppButton excelButton;

	@Override
	protected List<AppButton> addButtons() {
		createButtons();
		setOnButtonClick();
		return Arrays.asList(mailButton, excelButton);
	}

	protected void createButtons() {
		mailButton.icon("mail").tooltip("E-mail this...").build();
		excelButton.icon("excel").tooltip("Save to\na spreadsheet...").build();
	}

	protected void setOnButtonClick() {
		mailButton.setOnAction(e -> tryMailingCurrentlyDisplayedTableItems());
		excelButton.setOnAction(e -> trySavingCurrentlyDisplayedTableItemsToASpreadsheet());
	}

	private void tryMailingCurrentlyDisplayedTableItems() {
		try {
			// showTheNextMonthAging();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void trySavingCurrentlyDisplayedTableItemsToASpreadsheet() {
		try {
			service.saveAsExcel(table);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}
}
