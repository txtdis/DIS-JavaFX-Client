package ph.txtdis.app;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import ph.txtdis.dto.SalesRevenue;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.dialog.OpenByDateRangesDialog;
import ph.txtdis.fx.table.SalesRevenueTable;
import ph.txtdis.service.SalesRevenueService;

@Scope("prototype")
@Component("salesRevenueApp")
public class SalesRevenueApp extends AbstractExcelApp<SalesRevenueTable, SalesRevenueService, SalesRevenue> {

	@Autowired
	private TotaledTableApp totaledTableApp;

	@Autowired
	private AppButton backButton;

	@Autowired
	private AppButton openButton;

	@Autowired
	private AppButton nextButton;

	@Autowired
	private OpenByDateRangesDialog openDialog;

	@Override
	public void refresh() {
		try {
			table.items(service.list());
			totaledTableApp.refresh(service);
			super.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void start() {
		totaledTableApp.addTotalDisplayPane(1);
		super.start();
		verifyAllPickedSalesOrderHaveBeenBilled();
	}

	private void displayOpenByDateDialog() {
		openDialog.header("Enter Report Dates");
		openDialog.addParent(this).start();
	}

	private void showDateRangeCategoryRevenue() throws Exception {
		displayOpenByDateDialog();
		service.setStartDate(openDialog.getStartDate());
		service.setEndDate(openDialog.getEndDate());
		refresh();
	}

	private void showNextDay() {
		try {
			service.next();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void showPerDateRange() {
		try {
			showDateRangeCategoryRevenue();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void showPreviousDay() {
		try {
			service.previous();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void verifyAllPickedSalesOrderHaveBeenBilled() {
		try {
			// service.verifyAllPickedSalesOrderHaveBeenBilled();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
			close();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> superList = super.addButtons();
		List<AppButton> list = new ArrayList<>(asList(backButton, openButton, nextButton));
		list.addAll(superList);
		return list;
	}

	@Override
	protected void createButtons() {
		backButton.icon("back").tooltip("Back...").build();
		openButton.icon("openByDate").tooltip("Dates...").build();
		nextButton.icon("next").tooltip("Next...").build();
		super.createButtons();
	}

	@Override
	protected String getHeaderText() {
		return service.getHeaderText();
	}

	@Override
	protected String getTitleText() {
		return service.getTitleText();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(totaledTableApp.addTablePane(table));
	}

	@Override
	protected void setOnButtonClick() {
		backButton.setOnAction(e -> showPreviousDay());
		openButton.setOnAction(e -> showPerDateRange());
		nextButton.setOnAction(e -> showNextDay());
		super.setOnButtonClick();
	}
}
