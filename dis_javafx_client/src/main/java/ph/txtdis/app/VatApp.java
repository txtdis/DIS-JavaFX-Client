package ph.txtdis.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Vat;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.dialog.OpenByDateDialog;
import ph.txtdis.fx.table.VatTable;
import ph.txtdis.service.VatService;

@Component("vatApp")
public class VatApp extends AbstractTotaledApp<VatTable, VatService, Vat> {

	@Autowired
	private AppButton backButton;

	@Autowired
	private AppButton openButton;

	@Autowired
	private AppButton nextButton;

	@Autowired
	private OpenByDateDialog openDialog;

	@Override
	public void start() {
		createTotalDisplays(2);
		super.start();
	}

	private void showTheNextMonthVat() throws Exception {
		service.next();
		refresh();
	}

	private void showThePreviousMonthVat() throws Exception {
		service.previous();
		refresh();
	}

	private void showTheSelectedMonthVat() throws Exception {
		startOpenByDateDialog();
		service.setDate(openDialog.getDate());
		refresh();
	}

	private void startOpenByDateDialog() {
		openDialog.header("List a Month's VAT");
		openDialog.criteria("Enter a date of the desired month");
		openDialog.addParent(this).start();
	}

	private void tryShowingNextMonthVat() {
		try {
			showTheNextMonthVat();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void tryShowingPreviousMonthVat() {
		try {
			showThePreviousMonthVat();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void tryShowingSelectedMonthVat() {
		try {
			showTheSelectedMonthVat();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> superList = super.addButtons();
		List<AppButton> list = new ArrayList<>(Arrays.asList(backButton, openButton, nextButton));
		list.addAll(superList);
		return list;
	}

	@Override
	protected void createButtons() {
		backButton.icon("back").tooltip("Back...").build();
		openButton.icon("openByDate").tooltip("Open...").build();
		nextButton.icon("next").tooltip("Next...").build();
		super.createButtons();
	}

	@Override
	protected void setOnButtonClick() {
		backButton.setOnAction(e -> tryShowingPreviousMonthVat());
		openButton.setOnAction(e -> tryShowingSelectedMonthVat());
		nextButton.setOnAction(e -> tryShowingNextMonthVat());
		super.setOnButtonClick();
	}
}
