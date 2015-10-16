package ph.txtdis.app;

import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Invoice;
import ph.txtdis.dto.SoldDetail;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.InvoiceTable;
import ph.txtdis.info.Information;
import ph.txtdis.service.InvoiceService;

@Scope("prototype")
@Component("invoiceApp")
public class InvoiceApp extends AbstractIdApp<Invoice, InvoiceService, Long, String> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<BigDecimal> actualInput;

	@Autowired
	private AppField<Long> bookingIdInput;

	@Autowired
	private AppField<Long> invoiceIdInput;

	@Autowired
	private AppField<Long> customerIdDisplay;

	@Autowired
	private AppField<String> customerNameDisplay;

	@Autowired
	private AppField<String> customerAddressDisplay;

	@Autowired
	private AppField<String> invoiceIdPrefixInput;

	@Autowired
	private AppField<String> invoiceIdSuffixInput;

	@Autowired
	private AppField<String> remarksInput;

	@Autowired
	private LocalDatePicker orderDatePicker;

	@Autowired
	private AppField<LocalDate> dueDateDisplay;

	@Autowired
	private AppGridPane gridPane;

	@Autowired
	private AppCombo<String> discountCombo;

	@Autowired
	private AppField<BigDecimal> vatableDisplay;

	@Autowired
	private AppField<BigDecimal> vatDisplay;

	@Autowired
	private AppField<BigDecimal> totalDisplay;

	@Autowired
	private AppCombo<String> paymentCombo;

	@Autowired
	private AppField<BigDecimal> balanceDisplay;

	@Autowired
	private InvoiceTable table;

	@Override
	public void refresh() {
		invoiceIdPrefixInput.setValue(service.getPrefix());
		invoiceIdInput.setValue(service.getNbrId());
		invoiceIdSuffixInput.setValue(service.getSuffix());
		bookingIdInput.setValue(service.getBookingId());
		actualInput.setValue(service.get().getActualValue());
		orderDatePicker.setValue(service.get().getOrderDate());
		dueDateDisplay.setValue(service.getDueDate());
		customerIdDisplay.setValue(service.getCustomerId());
		customerNameDisplay.setValue(service.getCustomerName());
		customerAddressDisplay.setValue(service.getCustomerAddress());
		remarksInput.setValue(service.get().getRemarks());
		table.items(service.getDetails());
		refreshSummaryPane();
		super.refresh();
	}

	@Override
	public void save() {
		try {
			service.save();
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
			e.printStackTrace();
		} catch (Information i) {
			dialog.show(i).addParent(this).start();
			refresh();
		}
	}

	@Override
	public void setFocus() {
		newButton.requestFocus();
	}

	private HBox customerBox() {
		return new HBox(customerIdDisplay.readOnly().build(ID), customerNameDisplay.readOnly().width(375).build(TEXT));
	}

	private void handleError(Exception e, InputControl<?> control) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		control.setValue(null);
		((Node) control).requestFocus();
	}

	private HBox invoiceIdBox() {
		return new HBox(invoiceIdPrefixInput.width(70).build(CODE), invoiceIdInput.build(ID),
				invoiceIdSuffixInput.width(40).build(CODE));
	}

	private void openByInvoiceId(String id) throws Exception {
		Invoice i = service.findByInvoiceId(id);
		service.set(i);
		refresh();
	}

	private Node payment() {
	// @formatter:off
		return box.hpane(
			label.name("Discount"), discountCombo.readOnlyOfWidth(180),
			label.name("Payment"), paymentCombo.readOnlyOfWidth(320),
			label.name("Balance"), balanceDisplay.readOnly().build(CURRENCY));
	// @formatter:on
	}

	private void refreshSummaryPane() {
		try {
			discountCombo.items(service.getDiscountTextList());
			vatableDisplay.setValue(service.getVatableValue());
			vatDisplay.setValue(service.getVatValue());
			totalDisplay.setValue(service.getTotalValue());
			paymentCombo.items(service.getPaymentList());
			balanceDisplay.setValue(service.getBalance());
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void setBindings() {
		saveButton.disableIf((table.isEmpty().and(remarksInput.isNot("CANCELLED"))));
		invoiceIdPrefixInput.disableIf(orderDatePicker.isEmpty().or(posted()));
		invoiceIdInput.disableIf(orderDatePicker.isEmpty().or(posted()));
		invoiceIdSuffixInput.disableIf(invoiceIdInput.isEmpty().or(posted()));
		actualInput.disableIf(invoiceIdInput.isEmpty().or(posted()));
		bookingIdInput.disableIf(actualInput.isEmpty().or(posted()));
		remarksInput.disableIf(bookingIdInput.isEmpty().or(posted()));
		table.disableIf(bookingIdInput.isEmpty().or(posted()));
	}

	private void tryOpeningByInvoiceId(String id) {
		try {
			openByInvoiceId(id);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void updateSummaryPane(ObservableList<SoldDetail> c) {
		service.setDetails(c);
		refreshSummaryPane();
	}

	private void updateUponBookingIdValidation() {
		try {
			service.updateUponBookingIdValidation(bookingIdInput.getValue());
			refresh();
		} catch (Exception e) {
			service.resetBooking();
			refresh();
			handleError(e, bookingIdInput);
		}
	}

	private void updateUponDateValidation() {
		try {
			service.setOrderDateUponValidation(orderDatePicker.getValue());
		} catch (Exception e) {
			handleError(e, orderDatePicker);
		}
	}

	private void updateUponInvoiceIdValidation() {
		try {
			service.updateUponInvoiceIdValidation(invoiceIdPrefixInput.getValue(), invoiceIdInput.getValue(),
					invoiceIdSuffixInput.getValue());
		} catch (Exception e) {
			invoiceIdSuffixInput.setValue(null);
			invoiceIdInput.setValue(null);
			handleError(e, invoiceIdPrefixInput);
		}
	}

	private Node vat() {
	// @formatter:off
		return box.hpane(
			label.name("VATable"), vatableDisplay.readOnly().build(CURRENCY),
			label.name("VAT"), vatDisplay.readOnly().build(CURRENCY),
			label.name("Total"), totalDisplay.readOnly().build(CURRENCY));
	// @formatter:on
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		setBindings();
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(orderDatePicker, 1, 0, 2, 1);
		gridPane.add(label.field("S/I No."), 3, 0);
		gridPane.add(invoiceIdBox(), 4, 0);
		gridPane.add(label.field("Amount"), 5, 0);
		gridPane.add(actualInput.build(CURRENCY), 6, 0);
		gridPane.add(label.field("S/O No."), 7, 0);
		gridPane.add(bookingIdInput.build(ID), 8, 0);
		gridPane.add(label.field("Due"), 0, 1);
		gridPane.add(dueDateDisplay.readOnly().build(DATE), 1, 1);
		gridPane.add(label.field("Customer"), 2, 1, 2, 1);
		gridPane.add(customerBox(), 4, 1, 5, 1);
		gridPane.add(label.field("Address"), 0, 2);
		gridPane.add(customerAddressDisplay.readOnly().build(TEXT), 1, 2, 8, 1);
		gridPane.add(label.field("Remarks"), 0, 3);
		gridPane.add(remarksInput.build(TEXT), 1, 3, 8, 1);
		return Arrays.asList(gridPane, box.hpane(table.build()), vat(), payment());
	}

	@Override
	protected String moduleId() {
		return service.getModuleId() + service.getOrderNo();
	}

	@Override
	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			tryOpeningByInvoiceId(id);
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		orderDatePicker.setOnAction(a -> updateUponDateValidation());
		invoiceIdSuffixInput.setOnAction(a -> updateUponInvoiceIdValidation());
		actualInput.setOnAction(a -> service.get().setActualValue(actualInput.getValue()));
		bookingIdInput.setOnAction(a -> updateUponBookingIdValidation());
		remarksInput.setOnAction(a -> service.get().setRemarks(remarksInput.getValue()));
		table.setOnItemChange(i -> updateSummaryPane(table.getItems()));
	}
}
