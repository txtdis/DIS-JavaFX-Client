package ph.txtdis.app;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.CreditNote;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.CreditNoteDumpTable;
import ph.txtdis.fx.table.CreditNotePaymentTable;
import ph.txtdis.service.CreditNoteService;

@Lazy
@Component("creditNoteApp")
public class CreditNoteApp extends AbstractIdApp<CreditNoteService, Long, Long> {

	@Autowired
	private AppButton unpaidListButton, dataDumpButton;

	@Autowired
	private AppField<BigDecimal> balanceDisplay, totalPaymentDisplay, valueField;

	@Autowired
	private AppField<Long> idDisplay;

	@Autowired
	private AppField<String> descriptionField, lastModifiedByDisplay, remarksField;

	@Autowired
	private AppField<ZonedDateTime> lastModifiedOnDisplay;

	@Autowired
	private CreditNoteListApp listApp;

	@Autowired
	private CreditNoteDumpTable dataDumpTable;

	@Autowired
	private CreditNotePaymentTable paymentTable;

	@Autowired
	private LocalDatePicker datePicker;

	@Override
	public void refresh() {
		super.refresh();
		idDisplay.setValue(service.getId());
		datePicker.setValue(creditNote().getCreditDate());
		valueField.setValue(service.getTotal());
		descriptionField.setValue(creditNote().getDescription());
		remarksField.setValue(creditNote().getRemarks());
		paymentTable.items(creditNote().getPayments());
		lastModifiedByDisplay.setValue(creditNote().getLastModifiedBy());
		lastModifiedOnDisplay.setValue(creditNote().getLastModifiedOn());
	}

	@Override
	public void save() {
		creditNote().setCreditDate(datePicker.getValue());
		creditNote().setDescription(descriptionField.getValue());
		creditNote().setRemarks(remarksField.getValue());
		super.save();
	}

	@Override
	public void setFocus() {
		if (isNew())
			datePicker.requestFocus();
	}

	private CreditNote creditNote() {
		return service.get();
	}

	private void dumpData() {
		try {
			service.saveDataDumpAsExcel(dataDumpTable.build());
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(datePicker, 1, 0);
		gridPane.add(label.field("Amount"), 2, 0);
		gridPane.add(valueField.build(CURRENCY), 3, 0);
		gridPane.add(label.field("ID No."), 4, 0);
		gridPane.add(idDisplay.readOnly().build(ID), 5, 0);
		gridPane.add(label.field("Description"), 0, 1);
		gridPane.add(descriptionField.build(TEXT), 1, 1, 5, 1);
		gridPane.add(label.field("Remarks"), 0, 2);
		gridPane.add(remarksField.build(TEXT), 1, 2, 5, 1);
		return gridPane;
	}

	private BooleanBinding noDescription() {
		return descriptionField.isEmpty();
	}

	private Node paymentPane() {
		return box.forHorizontalPane(asList(//
				label.name("Balance"), balanceDisplay.readOnly().build(CURRENCY), //
				label.name("Total"), totalPaymentDisplay.readOnly().build(CURRENCY)));
	}

	private void setValue() {
		if (isNew())
			service.updateTotals(valueField.getValue());
	}

	private HBox tablePane() {
		return box.forHorizontalPane(paymentTable.build());
	}

	private void updatePaymentNodes() {
		totalPaymentDisplay.setValue(service.getPayment());
		balanceDisplay.setValue(service.getBalance());
	}

	private void updatePaymentPane() {
		service.updatePayments(paymentTable.getItems());
		updatePaymentNodes();
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> b = new ArrayList<>(super.addButtons());
		b.add(unpaidListButton.icon("list").tooltip("List partially paid...").build());
		b.add(dataDumpButton.icon("dataDump").tooltip("Dump data...").build());
		return b;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(gridPane(), tablePane(), paymentPane(), trackedPane());
	}

	@Override
	protected void setBindings() {
		super.setBindings();
		datePicker.disableIf(isPosted());
		valueField.disableIf(isPosted()//
				.or(totalPaymentDisplay.isNotEmpty())//
				.or(datePicker.isEmpty()));
		descriptionField.disableIf(isPosted()//
				.or(valueField.isEmpty()));
		paymentTable.disableIf(noDescription());
		saveButton.disableIf(noDescription());
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		unpaidListButton.setOnAction(e -> listApp.addParent(this).start());
		dataDumpButton.setOnAction(e -> dumpData());
		valueField.setOnAction(e -> setValue());
		paymentTable.setOnItemChange(i -> updatePaymentPane());
	}

	@Override
	protected HBox trackedPane() {
		HBox b = super.trackedPane();
		b.getChildren()
				.addAll(asList(//
						label.name("Last Modified by"), lastModifiedByDisplay.readOnly().width(120).build(TEXT), //
						label.name("on"), lastModifiedOnDisplay.readOnly().build(TIMESTAMP)));
		return b;
	}
}
