package ph.txtdis.app;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.table.AppTable;
import ph.txtdis.service.Excel;

public abstract class AbstractExcelApp<AT extends AppTable<T>, AS extends Excel<?>, T>
		extends AbstractTableApp<AT, AS, T>
{

	@Autowired
	private AppButton mailButton;

	@Autowired
	private AppButton excelButton;

	private void mailExcelFile() {
		try {
			// showTheNextMonthAging();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void saveAsExcel() {
		try {
			saveAsExcel(table);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		createButtons();
		setOnButtonClick();
		return asList(mailButton, excelButton);
	}

	protected void createButtons() {
		excelButton.icon("excel").tooltip("Save to a spreadsheet").build();
		mailButton.icon("mail").tooltip("E-mail spreadsheet").build();
	}

	protected void saveAsExcel(AT table) throws IOException {
		service.saveAsExcel(table);
	}

	protected void setOnButtonClick() {
		mailButton.setOnAction(e -> mailExcelFile());
		excelButton.setOnAction(e -> saveAsExcel());
	}
}
