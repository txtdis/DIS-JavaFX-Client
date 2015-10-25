package ph.txtdis.app;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Booking;
import ph.txtdis.dto.SoldOrderDetail;
import ph.txtdis.exception.BadCreditException;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.SoldOrderTable;
import ph.txtdis.info.Information;
import ph.txtdis.service.BookingService;

@Scope("prototype")
@Component("bookingApp")
public class BookingApp extends AbstractIdApp<Booking, BookingService, Long, String> {

	private static final String A_DAY_OVER = "1";

	private static final String MAX_DAYS_OVER = String.valueOf(Integer.MAX_VALUE);

	@Autowired
	private CustomerReceivableApp customerReceivableApp;

	@Autowired
	private AppCombo<String> discountCombo;

	@Autowired
	private AppField<LocalDate> dueDateDisplay;

	@Autowired
	private AppField<Long> idDisplay;

	@Autowired
	private AppField<Long> customerIdInput;

	@Autowired
	private AppField<String> customerNameDisplay;

	@Autowired
	private AppField<String> customerAddressDisplay;

	@Autowired
	private AppField<String> remarksInput;

	@Autowired
	private LabelFactory label;

	@Autowired
	private LocalDatePicker orderDatePicker;

	@Autowired
	private AppGridPane gridPane;

	@Autowired
	private AppField<BigDecimal> vatableDisplay;

	@Autowired
	private AppField<BigDecimal> vatDisplay;

	@Autowired
	private AppField<BigDecimal> totalDisplay;

	@Autowired
	private AppField<String> printedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> printedOnDisplay;

	@Autowired
	private SoldOrderTable table;

	@Override
	public void refresh() {
		idDisplay.setValue(service.getId());
		orderDatePicker.setValue(service.get().getOrderDate());
		dueDateDisplay.setValue(service.getDueDate());
		customerIdInput.setValue(service.getCustomerId());
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
		if (!service.isNew())
			return;
		if (customerNameDisplay.isEmpty().get())
			orderDatePicker.requestFocus();
		else
			table.requestFocus();
	}

	private HBox customerBox() {
		return new HBox(customerIdInput.build(ID), customerNameDisplay.readOnly().width(375).build(TEXT));
	}

	private String[] customerReceivableAppParams() {
		return new String[] { customerIdInput.getText(), A_DAY_OVER, MAX_DAYS_OVER };
	}

	private void openByBookingId(String id) throws Exception {
		Booking i = service.find(id);
		service.set(i);
		refresh();
	}

	private void refreshSummaryPane() {
		try {
			discountCombo.items(service.getDiscountTextList());
			vatableDisplay.setValue(service.getVatableValue());
			vatDisplay.setValue(service.getVatValue());
			totalDisplay.setValue(service.getTotalValue());
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private void setBindings() {
		saveButton.disableIf((table.isEmpty().and(remarksInput.isNot("CANCELLED"))).or(posted()));
		customerIdInput.disableIf(orderDatePicker.isEmpty());
		remarksInput.disableIf(customerNameDisplay.isEmpty());
		table.disableIf(customerNameDisplay.isEmpty());
	}

	private void showCustomerReceivableApp() {
		customerReceivableApp.addParent(this).start();
		customerReceivableApp.tryOpening(customerReceivableAppParams());
	}

	private void showStatementOfAccountThenClearControlAfterShowingErrorDialog(BadCreditException e) {
		showErrorDialog(e);
		showCustomerReceivableApp();
		clearControl(customerIdInput);
	}

	private HBox table() {
		return box.hpane(table.addService(service).build());
	}

	private void tryOpeningByBookingId(String id) {
		try {
			openByBookingId(id);
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private void updateSummaryPane(ObservableList<SoldOrderDetail> c) {
		System.err.println("\nupdating\n");
		service.setDetails(c);
		refreshSummaryPane();
	}

	private void updateUponCustomerIdValidation() {
		try {
			service.updateUponCustomerIdValidation(customerIdInput.getValue());
		} catch (BadCreditException e) {
			showStatementOfAccountThenClearControlAfterShowingErrorDialog(e);
		} catch (Exception e) {
			service.reset();
			clearControlAfterShowingErrorDialog(e, customerIdInput);
		} finally {
			refresh();
		}
	}

	private void updateUponDateValidation() {
		try {
			service.setOrderDateUponValidation(orderDatePicker.getValue());
			refresh();
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, orderDatePicker);
		}
	}

	private Node vat() {
	// @formatter:off
		return box.hpane(
			label.name("Discount"), discountCombo.readOnlyOfWidth(180),
			label.name("VATable"), vatableDisplay.readOnly().build(CURRENCY),
			label.name("VAT"), vatDisplay.readOnly().build(CURRENCY),
			label.name("Total"), totalDisplay.readOnly().build(CURRENCY));
	// @formatter:on
	}

	@Override
	protected HBox auditPane() {
	// @formatter:off
		HBox hbox = super.auditPane();
		hbox.getChildren().addAll(
			label.name("Printed by"), printedByDisplay.readOnly().width(120).build(TEXT),
			label.name("on"), printedOnDisplay.readOnly().build(TIMESTAMP));
		return hbox;
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
		gridPane.add(orderDatePicker, 1, 0);
		gridPane.add(label.field("Customer"), 2, 0);
		gridPane.add(customerBox(), 3, 0, 2, 1);
		gridPane.add(label.field("S/O No."), 5, 0);
		gridPane.add(idDisplay.build(ID), 6, 0);
		gridPane.add(label.field("Due"), 0, 1);
		gridPane.add(dueDateDisplay.readOnly().build(DATE), 1, 1);
		gridPane.add(label.field("Address"), 2, 1);
		gridPane.add(customerAddressDisplay.readOnly().build(TEXT), 3, 1, 3, 1);
		gridPane.add(label.field("Remarks"), 0, 2);
		gridPane.add(remarksInput.build(TEXT), 1, 2, 5, 1);
		return Arrays.asList(gridPane, table(), vat());
	}

	@Override
	protected String moduleId() {
		return service.getModuleId() + service.getId();
	}

	@Override
	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			tryOpeningByBookingId(id);
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		orderDatePicker.setOnAction(a -> updateUponDateValidation());
		customerIdInput.setOnAction(a -> updateUponCustomerIdValidation());
		remarksInput.setOnAction(a -> service.get().setRemarks(remarksInput.getValue()));
		table.setOnItemChange(i -> updateSummaryPane(table.getItems()));
	}

	@Override
	protected void updateAuditNodes() {
		super.updateAuditNodes();
		printedByDisplay.setValue(service.get().getPrintedBy());
		printedOnDisplay.setValue(service.get().getPrintedOn());
	}
}
