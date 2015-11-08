package ph.txtdis.app;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;
import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Billable;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.SoldOrderTable;
import ph.txtdis.service.InvoiceService;
import ph.txtdis.type.ModuleType;

@Scope("prototype")
@Component("invoiceApp")
public class InvoiceApp extends AbstractIdApp<Billable, InvoiceService, Long, String> implements MultiTyped {

	private static final String PROMPT = "Select date whose first entry will opened";

	@Autowired
	private AppButton openByDateButton;

	@Autowired
	private AppCombo<String> discountCombo;

	@Autowired
	private AppCombo<String> paymentCombo;

	@Autowired
	private AppField<BigDecimal> actualInput;

	@Autowired
	private AppField<LocalDate> dueDateDisplay;

	@Autowired
	private AppField<Long> bookingIdInput;

	@Autowired
	private AppField<Long> idNoInput;

	@Autowired
	private AppField<Long> customerIdDisplay;

	@Autowired
	private AppField<String> customerNameDisplay;

	@Autowired
	private AppField<String> customerAddressDisplay;

	@Autowired
	private AppField<String> idPrefixInput;

	@Autowired
	private AppField<String> idSuffixInput;

	@Autowired
	private AppField<BigDecimal> vatableDisplay;

	@Autowired
	private AppField<BigDecimal> vatDisplay;

	@Autowired
	private AppField<BigDecimal> totalDisplay;

	@Autowired
	private AppField<BigDecimal> balanceDisplay;

	@Autowired
	private LocalDatePicker orderDatePicker;

	@Autowired
	private SoldOrderTable table;

	private Label billableIdLabel, actualAmountLabel;

	private ModuleType type;

	@Override
	public void refresh() {
		idPrefixInput.setValue(service.get().getPrefix());
		idNoInput.setValue(idNo());
		idSuffixInput.setValue(service.get().getSuffix());
		bookingIdInput.setValue(service.get().getBookingId());
		actualInput.setValue(service.get().getActualValue());
		orderDatePicker.setValue(service.get().getOrderDate());
		dueDateDisplay.setValue(service.get().getDueDate());
		customerIdDisplay.setValue(service.get().getCustomerId());
		customerNameDisplay.setValue(service.get().getCustomerName());
		customerAddressDisplay.setValue(service.get().getCustomerAddress());
		table.items(service.get().getDetails());
		remarksDisplay.setValue(service.getRemarks());
		auditedApp.refresh(service);
		refreshSummaryPane();
		super.refresh();
	}

	@Override
	public void setFocus() {
		if (service.isNew())
			orderDatePicker.requestFocus();
	}

	@Override
	public void start() {
		service.setType(type);
		super.start();
	}

	@Override
	public String type() {
		String s = capitalizeFully(type.toString(), '_').replace("_", "");
		return uncapitalize(s);
	}

	@Override
	public Startable type(ModuleType type) {
		this.type = type;
		return this;
	}

	private AppField<BigDecimal> actualAmountInput() {
		actualInput.build(CURRENCY);
		actualInput.visibleProperty().bind(idNoInput.visibleProperty());
		return actualInput;
	}

	private Label actualAmountLabel() {
		actualAmountLabel = label.field("Amount");
		actualAmountLabel.visibleProperty().bind(idNoInput.visibleProperty());
		return actualAmountLabel;
	}

	private Node billableIdBox() {
		HBox b = new HBox(idPrefixInput.width(70).build(CODE), idNoInput.build(ID),
				idSuffixInput.width(40).build(CODE));
		return service.isAnInvoice() ? b : idNoDisplay();
	}

	private Label billableIdLabel() {
		billableIdLabel = label.field(service.getModuleId());
		billableIdLabel.visibleProperty().bind(idNoInput.visibleProperty());
		return billableIdLabel;
	}

	private HBox customerBox() {
		return new HBox(customerIdDisplay.readOnly().build(ID), customerNameDisplay.readOnly().width(375).build(TEXT));
	}

	private Node discountedPaymentPane() {
		return discountPane(paymentNodes());
	}

	private Node discountedTotalPane() {
		return discountPane(totalNodes());
	}

	private Node discountedVatPane() {
		return discountPane(vatNodes());
	}

	private List<Node> discountNodes() {
		List<Node> l = Arrays.asList(label.name("Discount"), discountCombo.readOnlyOfWidth(180));
		return new ArrayList<>(l);
	}

	private Node discountPane(List<Node> nodes) {
		List<Node> list = discountNodes();
		list.addAll(nodes);
		return box.forHorizontalPane(list);
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(orderDatePicker, 1, 0, 2, 1);
		gridPane.add(billableIdLabel(), 3, 0);
		gridPane.add(billableIdBox(), 4, 0);
		gridPane.add(actualAmountLabel(), 5, 0);
		gridPane.add(actualAmountInput(), 6, 0);
		gridPane.add(label.field("S/O No."), 7, 0);
		gridPane.add(bookingIdInput.build(ID), 8, 0);
		gridPane.add(label.field("Due"), 0, 1);
		gridPane.add(dueDateDisplay.readOnly().build(DATE), 1, 1);
		gridPane.add(label.field("Customer"), 2, 1, 2, 1);
		gridPane.add(customerBox(), 4, 1, 6, 1);
		gridPane.add(label.field("Address"), 0, 2);
		gridPane.add(customerAddressDisplay.readOnly().build(TEXT), 1, 2, 8, 1);
		gridPane.add(label.field("Remarks"), 0, 3);
		gridPane.add(remarksDisplay.readOnly().build(TEXT), 1, 3, 8, 1);
		return gridPane;
	}

	private void handleError(Exception e, InputControl<?> control) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		control.setValue(null);
		((Node) control).requestFocus();
	}

	private Long idNo() {
		if (service.isABooking())
			return service.getBookingId();
		return service.getNumId();
	}

	private Node idNoDisplay() {
		idNoInput.disableProperty().unbind();
		idNoInput.setMinWidth(idNoInput.getWidth() + 110);
		idNoInput.setDisable(true);
		return idNoInput;
	}

	private void openByOrderNo(String id) throws Exception {
		Billable i = service.findByOrderNo(id);
		service.set(i);
		refresh();
	}

	private List<Node> paymentNodes() {
	// @formatter:off
		return Arrays.asList(
			label.name("Payment"), paymentCombo.readOnlyOfWidth(390),
			label.name("Balance"), balanceDisplay.readOnly().build(CURRENCY));
	// @formatter:on
	}

	private Node paymentPane() {
		return box.forHorizontalPane(paymentNodes());
	}

	private void refreshPaymentNodes() {
		if (balanceDisplay == null)
			return;
		paymentCombo.items(service.get().getPayments());
		balanceDisplay.setValue(service.getBalance());
	}

	private void refreshSummaryPane() {
		try {
			discountCombo.items(service.get().getDiscounts());
			totalDisplay.setValue(service.get().getTotalValue());
			refreshVatNodes();
			refreshPaymentNodes();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void refreshVatNodes() throws Exception {
		if (vatableDisplay == null)
			return;
		vatableDisplay.setValue(service.getVatable());
		vatDisplay.setValue(service.getVat());
	}

	private void setBindings() {
		saveButton.disableIf((table.isEmpty().and(remarksDisplay.isNot("CANCELLED"))).or(posted()));
		auditButton.disableIf(notPosted());
		idPrefixInput.disableIf(orderDatePicker.isEmpty());
		idNoInput.disableIf(orderDatePicker.isEmpty());
		idSuffixInput.disableIf(idNoInput.isEmpty());
		actualInput.disableIf(idNoInput.isEmpty());
		bookingIdInput.disableIf(actualInput.isEmpty());
		table.disableIf(bookingIdInput.isEmpty());
		auditedApp.hideAuditNodesIf(idNoInput.visibleProperty());
		if (service.isABooking())
			idNoInput.setVisible(false);
	}

	private void showAuditDialogToValidateOrder() {
		auditDialog.addParent(this).start();
		if (auditDialog.isValid() != null)
			saveAudit();
	}

	private void showOpenByDateDialog() {
		String h = service.getOpenDialogHeading();
		dateDialog.header(h).prompt(PROMPT).addParent(this).start();
		LocalDate d = dateDialog.getDate();
		if (d != null)
			open(d);
	}

	private HBox table() {
		return box.forHorizontalPane(table.addService(service).build());
	}

	private List<Node> totalNodes() {
		return Arrays.asList(label.name("Total"), totalDisplay.readOnly().build(CURRENCY));
	}

	private void tryOpeningByOrderNo(String id) {
		try {
			openByOrderNo(id);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void updateSummaryPane() {
		service.setDetails(table.getItems());
		refreshSummaryPane();
	}

	private void updateUponBookingIdValidation() {
		try {
			service.updateUponBookingIdValidation(bookingIdInput.getValue());
		} catch (Exception e) {
			handleError(e, bookingIdInput);
		} finally {
			refresh();
		}
	}

	private void updateUponDateValidation() {
		try {
			service.setOrderDateUponValidation(orderDatePicker.getValue());
		} catch (Exception e) {
			handleError(e, orderDatePicker);
		} finally {
			refresh();
		}
	}

	private void updateUponOrderNoValidation() {
		try {
			service.updateUponOrderNoValidation(idPrefixInput.getValue(), idNoInput.getValue(),
					idSuffixInput.getValue());
		} catch (Exception e) {
			idSuffixInput.setValue(null);
			idNoInput.setValue(null);
			handleError(e, idPrefixInput);
		}
	}

	private List<Node> vatNodes() {
	// @formatter:off
		List<Node> l = new ArrayList<>(Arrays.asList(
			label.name("VATable"), vatableDisplay.readOnly().build(CURRENCY),
			label.name("VAT"), vatDisplay.readOnly().build(CURRENCY)));
		l.addAll(totalNodes());
		return l;
	// @formatter:on
	}

	private Node vatPane() {
		return box.forHorizontalPane(vatNodes());
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> b = new ArrayList<>(super.addButtons());
		b.add(2, openByDateButton.icon("openByDate").tooltip("Open a date's\nfirst entry").build());
		b.add(auditButton = auditedApp.addAuditButton());
		return b;
	}

	@Override
	protected HBox auditPane() {
		return auditedApp.addAuditDisplays(super.auditPane());
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		setBindings();
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		if (service.isADeliveryReport())
			return asList(gridPane(), table(), discountedTotalPane(), paymentPane());
		if (service.isABooking())
			return asList(gridPane(), table(), discountedVatPane());
		return asList(gridPane(), table(), vatPane(), discountedPaymentPane());
	}

	@Override
	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			tryOpeningByOrderNo(id);
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		orderDatePicker.setOnAction(a -> updateUponDateValidation());
		idSuffixInput.setOnAction(a -> updateUponOrderNoValidation());
		actualInput.setOnAction(a -> service.get().setActualValue(actualInput.getValue()));
		bookingIdInput.setOnAction(a -> updateUponBookingIdValidation());
		table.setOnItemChange(i -> updateSummaryPane());
		openByDateButton.setOnAction(e -> showOpenByDateDialog());
		auditedApp.setOnAuditButtonClick(e -> showAuditDialogToValidateOrder());
	}
}
