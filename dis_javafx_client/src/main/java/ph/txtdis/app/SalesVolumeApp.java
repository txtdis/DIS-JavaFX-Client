package ph.txtdis.app;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.SalesVolume;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.dialog.OpenByDateRangesDialog;
import ph.txtdis.fx.table.SalesVolumeTable;
import ph.txtdis.service.SalesVolumeService;

@Scope("prototype")
@Component("salesVolumeApp")
public class SalesVolumeApp extends AbstractExcelApp<SalesVolumeTable, SalesVolumeService, SalesVolume> {

	@Autowired
	private AppButton backButton, openButton, nextButton, dataDumpButton;

	@Autowired
	private SalesVolumeTable dataDumpTable;

	@Autowired
	private OpenByDateRangesDialog openDialog;

	private Label subhead;

	@Override
	public void refresh() {
		try {
			table.items(service.list());
			refreshSubheader();
			super.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void start() {
		super.start();
		verifyAllPickedSalesOrderHaveBeenBilled();
	}

	private void displayOpenByDateDialog() {
		openDialog.header("Enter Report Dates");
		openDialog.addParent(this).start();
	}

	private void dumpData() {
		try {
			dataDumpTable.items(service.dataDump());
			service.saveAsExcel(dataDumpTable);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void refreshSubheader() {
		subhead.setText(service.getSubhead());
		table.setId(service.getSubhead());
	}

	private void showDateRangeCategoryVolume() throws Exception {
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
			showDateRangeCategoryVolume();
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

	private HBox subheadPane() {
		subhead = label.subheader("");
		return box.forSubheader(subhead);
	}

	private HBox tablePane() {
		return box.forHorizontalPane(table.build());
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
		List<AppButton> list = new ArrayList<>(asList(backButton, openButton, nextButton));
		list.addAll(super.addButtons());
		list.add(dataDumpButton);
		return list;
	}

	@Override
	protected void createButtons() {
		backButton.icon("back").tooltip("Back...").build();
		openButton.icon("openByDate").tooltip("Dates...").build();
		nextButton.icon("next").tooltip("Next...").build();
		dataDumpButton.icon("dataDump").tooltip("Dump Data...").build();
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
		return asList(subheadPane(), tablePane());
	}

	@Override
	protected void setOnButtonClick() {
		backButton.setOnAction(e -> showPreviousDay());
		openButton.setOnAction(e -> showPerDateRange());
		nextButton.setOnAction(e -> showNextDay());
		dataDumpButton.setOnAction(e -> dumpData());
		super.setOnButtonClick();
	}
}
