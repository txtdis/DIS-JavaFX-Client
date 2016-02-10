package ph.txtdis.app;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static javafx.beans.binding.Bindings.equal;
import static javafx.beans.binding.Bindings.when;
import static javafx.collections.FXCollections.observableArrayList;
import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;
import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;
import static ph.txtdis.util.SpringUtil.username;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static ph.txtdis.type.ModuleType.BAD_ORDER;
import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.INVOICE;
import static ph.txtdis.type.ModuleType.PURCHASE_ORDER;
import static ph.txtdis.type.ModuleType.PURCHASE_RECEIPT;
import static ph.txtdis.type.ModuleType.RETURN_ORDER;
import static ph.txtdis.type.ModuleType.SALES_ORDER;
import static ph.txtdis.type.ModuleType.SALES_RETURN;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.Billable;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.exception.BadCreditException;
import ph.txtdis.exception.ExceededCreditLimitException;
import ph.txtdis.exception.NotAllowedOffSiteTransactionException;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.InputControl;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.dialog.ReturnPaymentDialog;
import ph.txtdis.fx.dialog.SearchDialog;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.BillableTable;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.service.BillableService;
import ph.txtdis.service.CustomerService;
import ph.txtdis.type.ModuleType;

@Scope("prototype")
@Component("salesApp")
public class SalesApp extends AbstractIdApp<BillableService, Long, String> implements MultiTyped {

	private static final String ON = "on";

	private static final String PROMPT = "Select date whose first entry will opened";

	@Autowired
	private AppButton customerSearchButton, invoiceBookletButton, openByDateButton, overrideButton, receiptButton,
			disposalButton, invalidateButton, paymentButton;

	@Autowired
	private AppCombo<String> discountCombo, paymentCombo;

	@Autowired
	private AppField<LocalDate> dueDateDisplay, orderDateDisplay;

	@Autowired
	private AppField<Long> bookingIdInput, idNoInput, customerIdInput, receivingIdDisplay;

	@Autowired
	private AppField<String> customerNameDisplay, customerAddressDisplay, idPrefixInput, idSuffixInput, billedByDisplay,
			modifiedReceivingByDisplay, printedByDisplay, receivedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> billedOnDisplay, modifiedReceivingOnDisplay, printedOnDisplay, receivedOnDisplay;

	@Autowired
	private AppField<BigDecimal> vatableDisplay, vatDisplay, totalDisplay, balanceDisplay, badOrderAllowanceDisplay,
			remainingBadOrderAllowanceDisplay;

	@Autowired
	private BillableTable table;

	@Autowired
	private CustomerListApp customerListApp;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InvoiceBookletApp invoiceBookletApp;

	@Autowired
	private LocalDatePicker orderDatePicker;

	@Autowired
	private ReturnPaymentDialog paymentDialog;

	@Autowired
	private SearchDialog searchDialog;

	private BooleanProperty salesOrderCanBeModified, salesReturnCanBeModified, returnIsValid;

	private ObjectProperty<ModuleType> type;

	public void addItemToTable(BillableDetail entity) {
		ObservableList<BillableDetail> l = observableArrayList(table.getItems());
		l.add(entity);
		table.setItems(l);
	}

	public void customerWithDueDateGridLine() {
		gridPane.add(label.field("Due"), 0, 1);
		gridPane.add(dueDateDisplay, 1, 1);
		gridPane.add(customerLabel(), 2, 1, 2, 1);
		gridPane.add(customerBox(), 4, 1, 8, 1);
	}

	public void customerWithoutDueDateGridLine() {
		gridPane.add(customerLabel(), 1, 1, 3, 1);
		gridPane.add(customerBox(), 4, 1, 6, 1);
	}

	public Customer getSelectionFromSearchResults() {
		customerListApp.addParent(this).start();
		return customerListApp.getSelection();
	}

	public boolean isASalesOrder() {
		return isSalesOrder().get();
	}

	@Override
	public void refresh() {
		refreshSummaryPane();
		refreshTrackedBillablePane();
		refreshTrackedReceivingModificationPane();
		refreshBadOrderAllowanceNodes();
		refreshReturnedItemNodes();
		super.refresh();
		salesReturnCanBeModified.set(service.salesReturnCanBeModified());
		returnIsValid.set(service.returnIsValid());
		idPrefixInput.setValue(billable().getPrefix());
		idNoInput.setValue(service.getIdNo());
		idSuffixInput.setValue(billable().getSuffix());
		bookingIdInput.setValue(billable().getBookingId());
		orderDatePicker.setValue(getOrderDate());
		orderDateDisplay.setValue(getOrderDate());
		receivingIdDisplay.setValue(billable().getReceivingId());
		refreshDateRelatedInputs();
		remarksDisplay.setValue(service.getRemarks());
		decisionNeededApp.refresh(service);
	}

	@Override
	public void save() {
		try {
			service.updateSummaries(table.getItems());
			service.save();
		} catch (SuccessfulSaveInfo i) {
			dialog.show(i).addParent(this).start();
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
		}
	}

	@Override
	public void setFocus() {
		if (isASalesOrPurchaseOrder())
			orderDatePicker.requestFocus();
		else if (isAnInvoice())
			idPrefixInput.requestFocus();
		else if (isABadOrder() || isAReturnOrder())
			customerIdInput.requestFocus();
		else
			bookingIdInput.requestFocus();
	}

	@Override
	public void start() {
		service.setType(type.get());
		super.start();
	}

	@Override
	public String type() {
		String s = type.get().toString();
		s = capitalizeFully(s, '_').replace("_", "");
		return uncapitalize(s);
	}

	@Override
	public Startable type(ModuleType t) {
		type = new SimpleObjectProperty<>(t);
		return this;
	}

	private void addAddressAndRemarks() {
		gridPane.add(label.field("Address"), 0, 2);
		gridPane.add(customerAddressDisplay, 1, 2, 8, 1);
		gridPane.add(label.field("Remarks"), 0, 3);
		gridPane.add(remarksDisplay, 1, 3, 8, 1);
	}

	private void addAddressAndRemarksIfNotReceiving() {
		customerAddressDisplay.readOnly().build(TEXT);
		remarksDisplay.readOnly().build(TEXT);
		if (notSalesReturn().get())
			addAddressAndRemarks();
	}

	private List<Node> addDecisionNodes() {
		if (notBillable())
			return decisionNeededApp.addApprovalNodes();
		return decisionNeededApp.addAuditNodes();
	}

	private HBox auditPane() {
		List<Node> l = new ArrayList<>(trackedPane().getChildren());
		l.addAll(addDecisionNodes());
		return box.forHorizontalPane(l);
	}

	private List<Node> badOrderAllowanceNodes() {
		List<Node> l = asList(//
				label.name("Bad Order Allowance"), //
				badOrderAllowanceDisplay.readOnly().build(CURRENCY));
		return new ArrayList<>(l);
	}

	private Node badOrderAllowancePane() {
		List<Node> l = badOrderAllowanceNodes();
		l.addAll(vatNodes());
		l.addAll(remainingBadOrderAllowanceNodes());
		return box.forHorizontalPane(l);
	}

	private Billable billable() {
		return service.get();
	}

	private String billingPrompt() {
		if (isABadOrder() || isAReturnOrder())
			return "Paid";
		return isAnInvoice() ? "Invoiced" : "D/R'd";
	}

	private void buildButttons() {
		openByDateButton.icon("openByDate").tooltip("Open a date's\nfirst entry").build();
		invoiceBookletButton.icon("invoiceBooklet").tooltip("Issue booklet...").build();
		receiptButton.icon("returnReceipt").tooltip("Receive...").build();
		invalidateButton.icon("deactivate").tooltip("Invalidate...").build();
		paymentButton.icon("cheque").tooltip("Pay...").build();
		disposalButton.icon("disposal").tooltip("Dispose...").build();
		overrideButton.icon("crown").tooltip("Override\ninvalidated S/O").build();
	}

	private void buildDisplays() {
		receivingIdDisplay.readOnly().build(ID);
		dueDateDisplay.readOnly().build(DATE);
		totalDisplay.readOnly().build(CURRENCY);
		paymentCombo.readOnlyOfWidth(420);
		balanceDisplay.readOnly().build(CURRENCY);
		discountCombo.readOnlyOfWidth(220);
		vatableDisplay.readOnly().build(CURRENCY);
		vatDisplay.readOnly().build(CURRENCY);
	}

	private HBox customerBox() {
		return new HBox(//
				customerIdInput.build(ID), //
				customerNameDisplay.readOnly().width(420).build(TEXT), //
				customerSearchButton.fontSize(16).icon("search").build() //
		);
	}

	private void customerGridLine() {
		if (isASalesReturn() || isAPurchaseReceipt() || isAPurchaseOrder() || isABadOrder() || isAReturnOrder())
			customerWithoutDueDateGridLine();
		else
			customerWithDueDateGridLine();
	}

	private ReadOnlyBooleanProperty customerIdInputIsDisabled() {
		return customerIdInput.disabledProperty();
	}

	private Label customerLabel() {
		return label.field("Customer");
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
		List<Node> l = asList(label.name("Discount"), discountCombo);
		return new ArrayList<>(l);
	}

	private Node discountPane(List<Node> nodes) {
		List<Node> list = discountNodes();
		list.addAll(nodes);
		return box.forHorizontalPane(list);
	}

	private LocalDate getOrderDate() {
		return billable().getOrderDate();
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(orderDate(), 1, 0, 2, 1);
		gridPane.add(idLabel(), 3, 0);
		gridPane.add(idBox(), 4, 0);
		gridPane.add(label.field(service.getReferenceName() + " No."), 5, 0);
		gridPane.add(bookingIdInput.build(ID), 6, 0);
		gridPane.add(label.field("R/R No."), 7, 0);
		gridPane.add(receivingIdDisplay, 8, 0);
		customerGridLine();
		addAddressAndRemarksIfNotReceiving();
		return gridPane;
	}

	private void handleError(Exception e, InputControl<?> control) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		control.setValue(null);
		((Node) control).requestFocus();
	}

	private Node idBox() {
		return isAnInvoice() ? threePartIdField() : idNoOnlyField();
	}

	private Label idLabel() {
		Label l = label.field(service.getIdPrompt() + " No.");
		l.visibleProperty().bind(idNoInput.visibleProperty());
		return l;
	}

	private Node idNoField() {
		return idNoInput.build(ID);
	}

	private Node idNoOnlyField() {
		return idNoInput.width(180).build(ID);
	}

	private Node idPrefixField() {
		return idPrefixInput.width(70).build(CODE);
	}

	private Node idSuffixField() {
		return idSuffixInput.width(40).build(CODE);
	}

	private void invalidateThis() {
		service.invalidate();
		save();
	}

	private boolean isABadOrder() {
		return isBadOrder().get();
	}

	private boolean isABill() {
		return isAnInvoice() || isADeliveryReport();
	}

	private boolean isADeliveryReport() {
		return type.get() == DELIVERY_REPORT;
	}

	private boolean isAnInvoice() {
		return isInvoice().get();
	}

	private boolean isAPurchaseOrder() {
		return isPurchaseOrder().get();
	}

	private boolean isAPurchaseReceipt() {
		return isPurchaseReceipt().get();
	}

	private boolean isAReturnOrder() {
		return isReturnOrder().get();
	}

	private boolean isASalesOrPurchaseOrder() {
		return isASalesOrder() || isAPurchaseOrder();
	}

	private boolean isASalesReturn() {
		return isSalesReturn().get();
	}

	private BooleanBinding isBadOrder() {
		return equal(BAD_ORDER, type);
	}

	private BooleanBinding isInvoice() {
		return equal(INVOICE, type);
	}

	private BooleanBinding isPurchaseOrder() {
		return equal(PURCHASE_ORDER, type);
	}

	private BooleanBinding isPurchaseReceipt() {
		return equal(PURCHASE_RECEIPT, type);
	}

	private BooleanBinding isReturnOrder() {
		return equal(RETURN_ORDER, type);
	}

	private BooleanBinding isSalesOrder() {
		return equal(SALES_ORDER, type);
	}

	private BooleanBinding isSalesReturn() {
		return equal(SALES_RETURN, type);
	}

	private List<Control> modifiedReceivingNodes() {
		return asList(//
				label.name(modifiedReceivingPrompt()), modifiedReceivingByDisplay.readOnly().width(120).build(TEXT), //
				label.name(ON), modifiedReceivingOnDisplay.readOnly().build(TIMESTAMP));
	}

	private String modifiedReceivingPrompt() {
		return (isABadOrder() ? "Disposed" : "Modified") + " by";
	}

	private BooleanBinding noIdNo() {
		return idNoInput.isEmpty();
	}

	private boolean notBillable() {
		return !(isAnInvoice() && isADeliveryReport());
	}

	private BooleanBinding notInvoice() {
		return isInvoice().not();
	}

	private BooleanBinding notReceived() {
		return receivedOnDisplay.isEmpty();
	}

	private BooleanBinding notSalesReturn() {
		return isSalesReturn().not();
	}

	private BooleanBinding notValidReturn() {
		return returnIsValid.not();
	}

	private void openByOrderNo(String id) throws Exception {
		Billable i = service.findById(id);
		service.set(i);
		refresh();
	}

	private void openSearchDialog() {
		customerIdInput.setValue(null);
		searchDialog.criteria("name").addParent(this).start();
		String name = searchDialog.getText();
		if (name != null)
			search(name);
	}

	private Node orderDate() {
		if (isASalesOrPurchaseOrder())
			return orderDatePicker;
		return orderDateDisplay.readOnly().build(DATE);
	}

	private List<Node> paymentNodes() {
		return asList(//
				label.name("Payment"), paymentCombo, //
				label.name("Balance"), balanceDisplay);
	}

	private Node paymentPane() {
		return box.forHorizontalPane(paymentNodes());
	}

	private void refreshBadOrderAllowanceNodes() {
		if (badOrderAllowanceDisplay == null)
			return;
		badOrderAllowanceDisplay.setValue(billable().getBadOrderAllowanceValue());
		refreshRemainingBadOrderAllowance();
	}

	private void refreshCustomerRelatedInputs() {
		orderDatePicker.setValue(getOrderDate());
		dueDateDisplay.setValue(billable().getDueDate());
		customerNameDisplay.setValue(billable().getCustomerName());
		customerAddressDisplay.setValue(billable().getCustomerAddress());
		table.items(service.getDetails());
		refreshBadOrderAllowanceNodes();
	}

	private void refreshDateRelatedInputs() {
		customerIdInput.setValue(service.getCustomerId());
		refreshCustomerRelatedInputs();
	}

	private void refreshPaymentNodes() {
		if (balanceDisplay != null) {
			paymentCombo.itemsSelectingFirst(billable().getPayments());
			balanceDisplay.setValue(service.getBalance());
		}
	}

	private void refreshRemainingBadOrderAllowance() {
		if (remainingBadOrderAllowanceDisplay != null)
			remainingBadOrderAllowanceDisplay.setValue(service.getRemainingBadOrderAllowance());
	}

	private void refreshReturnedItemNodes() {
		if (printedOnDisplay == null)
			return;
		printedByDisplay.setValue(billable().getPrintedBy());
		printedOnDisplay.setValue(billable().getPrintedOn());
		receivedByDisplay.setValue(billable().getReceivedBy());
		receivedOnDisplay.setValue(billable().getReceivedOn());
	}

	private void refreshSummaryPane() {
		discountCombo.itemsSelectingFirst(billable().getDiscounts());
		totalDisplay.setValue(billable().getTotalValue());
		refreshVatNodes();
		refreshPaymentNodes();
		refreshRemainingBadOrderAllowance();
	}

	private void refreshTrackedBillablePane() {
		if (billedOnDisplay != null) {
			billedByDisplay.setValue(billable().getBilledBy());
			billedOnDisplay.setValue(billable().getBilledOn());
		}
	}

	private void refreshTrackedReceivingModificationPane() {
		if (modifiedReceivingOnDisplay != null) {
			modifiedReceivingByDisplay.setValue(billable().getReceivingModifiedBy());
			modifiedReceivingOnDisplay.setValue(billable().getReceivingModifiedOn());
		}
	}

	private void refreshVatNodes() {
		if (vatableDisplay != null) {
			vatableDisplay.setValue(service.getVatable());
			vatDisplay.setValue(service.getVat());
		}
	}

	private List<Node> remainingBadOrderAllowanceNodes() {
		return asList(//
				label.name("Balance"), //
				remainingBadOrderAllowanceDisplay.readOnly().build(CURRENCY));
	}

	private Node returnedItemPane() {
		List<Node> list = new ArrayList<>(asList(//
				label.name("Printed by"), printedByDisplay.readOnly().width(120).build(TEXT), //
				label.name(ON), printedOnDisplay.readOnly().build(TIMESTAMP), //
				label.name("Received by"), receivedByDisplay.readOnly().width(120).build(TEXT), //
				label.name(ON), receivedOnDisplay.readOnly().build(TIMESTAMP)));
		if (isABadOrder())
			list.addAll(modifiedReceivingNodes()); // disposal nodes
		return box.forHorizontalPane(list);
	}

	private List<AppButton> returnItemButtons() {
		List<AppButton> l = new ArrayList<>(asList(receiptButton, invalidateButton, paymentButton));
		if (isABadOrder())
			l.add(2, disposalButton);
		return l;
	}

	private void saveDisposalData() {
		try {
			service.saveDisposalData();
			save();
		} catch (NotAllowedOffSiteTransactionException e) {
			showErrorDialog(e);
		}
	}

	private void saveInputtedItemReturnPaymentData() {
		paymentDialog.addParent(this).start();
		LocalDate d = paymentDialog.getAddedItem();
		saveItemReturnPaymentData(d);
	}

	private void saveItemReturnReceiptData() {
		try {
			service.saveItemReturnReceiptData();
			save();
		} catch (NotAllowedOffSiteTransactionException e) {
			showErrorDialog(e);
		}
	}

	private void saveItemReturnPaymentData(LocalDate d) {
		if (d != null) {
			service.setItemReturnPaymentData(d);
			save();
		} else
			service.clearItemReturnPaymentDataSetByItsInputDialogDuringDataEntry();
	}

	private void search(String name) {
		try {
			customerService.search(name);
			validateSelectedCustomerFromSearchList();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private void setCustomerDataAndTemporarilyInvalidateAwaitingApproval(String msg) {
		service.setCustomerRelatedData();
		service.updatePerValidity(false,
				"[INVALID: " + username() + " - " + toDateDisplay(now()) + "] " + msg.replace("\n", " "));
		decisionNeededApp.refresh(service);
	}

	private void setFocusAfterBookingIdValidation() {
		if (isASalesReturn())
			table.requestFocus();
		else if (isAnInvoice())
			saveButton.requestFocus();
	}

	private void setFocusAfterCustomerValidation() {
		if (customerNameDisplay.isEmpty().get())
			orderDatePicker.requestFocus();
		else
			table.requestFocus();
	}

	private void setNew() {
		if (isAnInvoice())
			isNew = billedOnDisplay.isEmpty().get();
		else
			isNew = createdOnDisplay.isEmpty().get();
	}

	private void showAuditDialogToValidateOrder() {
		decisionNeededApp.showDecisionDialogForValidation(this, service);
		refresh();
	}

	private void showOpenByDateDialog() {
		String h = service.getOpenDialogHeading();
		openByDateDialog.header(h).prompt(PROMPT).addParent(this).start();
		LocalDate d = openByDateDialog.getDate();
		if (d != null)
			open(d);
	}

	private void showProceedAndGetApprovalLaterOrExitDialog(String msg) {
		dialog.showOption(msg + "; proceed how?", "Book, get approval later", "Exit");
		dialog.setOnOptionSelection(e -> setCustomerDataAndTemporarilyInvalidateAwaitingApproval(msg));
		dialog.setOnDefaultSelection(e -> dialog.close());
		dialog.addParent(this).start();
	}

	private HBox tablePane() {
		return box.forHorizontalPane(table.build());
	}

	private HBox threePartIdField() {
		return new HBox(idPrefixField(), idNoField(), idSuffixField());
	}

	private List<Node> totalNodes() {
		return asList(label.name("Total"), totalDisplay);
	}

	private HBox trackedBilledPane() {
		return box.forHorizontalPane(asList(//
				label.name("Booked by"), createdByDisplay.readOnly().width(120).build(TEXT), //
				label.name(ON), createdOnDisplay.readOnly().build(TIMESTAMP), //
				label.name(billingPrompt() + " by"), billedByDisplay.readOnly().width(120).build(TEXT), //
				label.name(ON), billedOnDisplay.readOnly().build(TIMESTAMP)));//
	}

	private HBox trackedReceivingPane() {
		HBox h = super.trackedPane();
		h.getChildren().addAll(modifiedReceivingNodes());
		return h;
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
		service.updateSummaries(table.getItems());
		refreshSummaryPane();
	}

	private void updateUponBookingIdValidation(long id) {
		if (isNew() && id != 0)
			try {
				service.updateUponBookingIdValidation(id);
			} catch (Exception e) {
				handleError(e, bookingIdInput);
			} finally {
				refresh();
				setFocusAfterBookingIdValidation();
			}
	}

	private void updateUponCustomerValidation() {
		Long id = customerIdInput.getValue();
		if (isNew() && id != 0)
			try {
				service.updateUponCustomerIdValidation(id);
			} catch (BadCreditException | ExceededCreditLimitException e) {
				showProceedAndGetApprovalLaterOrExitDialog(e.getMessage());
			} catch (Exception e) {
				handleError(e, customerIdInput);
			} finally {
				refreshCustomerRelatedInputs();
				setFocusAfterCustomerValidation();
			}
	}

	private void updateUponDateValidation(LocalDate d) {
		if (isNew() && d != null && isASalesOrPurchaseOrder())
			try {
				service.setOrderDateUponValidation(d);
			} catch (Exception e) {
				handleError(e, orderDatePicker);
			} finally {
				refreshDateRelatedInputs();
			}
	}

	private void updateUponOrderNoValidation() {
		if (isNew())
			try {
				service.updateUponOrderNoValidation(idPrefixInput.getValue(), idNoInput.getValue(),
						idSuffixInput.getValue());
			} catch (Exception e) {
				idSuffixInput.setValue(null);
				idNoInput.setValue(null);
				handleError(e, idPrefixInput);
			}
	}

	private void validateSelectedCustomerFromSearchList() {
		Customer c = getSelectionFromSearchResults();
		if (c != null) {
			customerIdInput.setValue(c.getId());
			updateUponCustomerValidation();
		}
	}

	private List<Node> vatNodes() {
		List<Node> l = new ArrayList<>(Arrays.asList(//
				label.name("VATable"), vatableDisplay, //
				label.name("VAT"), vatDisplay));
		l.addAll(totalNodes());
		return l;
	}

	private Node vatPane() {
		return box.forHorizontalPane(vatNodes());
	}

	@Override
	protected List<AppButton> addButtons() {
		buildButttons();
		List<AppButton> b = new ArrayList<>(super.addButtons());
		b.add(2, openByDateButton);
		b.add(decisionButton = decisionNeededApp.addDecisionButton());
		if (isABadOrder() || isAReturnOrder())
			b.addAll(returnItemButtons());
		else if (isAnInvoice())
			b.add(invoiceBookletButton);
		else if (isASalesOrder())
			b.add(overrideButton);
		return b;
	}

	@Override
	protected String getTitleText() {
		return isNew() ? newModule() : service.getModuleId() + " " + service.getOrderNo();
	}

	@Override
	protected boolean isNew() {
		if (isNew == null)
			setNew();
		return isNew;
	}

	@Override
	protected BooleanExpression isPosted() {
		return isABill() ? billedOnDisplay.isEmpty().not() : super.isPosted();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		buildDisplays();
		List<Node> l = new ArrayList<>(asList(gridPane(), tablePane()));
		if (isADeliveryReport())
			l.addAll(asList(discountedTotalPane(), paymentPane()));
		else if (isABadOrder())
			l.addAll(asList(badOrderAllowancePane(), returnedItemPane()));
		else if (isAReturnOrder())
			l.addAll(asList(discountedVatPane(), returnedItemPane()));
		else if (isASalesOrder())
			l.addAll(asList(discountedVatPane()));
		else if (isAnInvoice())
			l.addAll(asList(vatPane(), discountedPaymentPane()));
		l.add(auditPane());
		return l;
	}

	@Override
	protected void openSelected() {
		String id = getDialogInput();
		if (id != null && !id.isEmpty())
			tryOpeningByOrderNo(id);
	}

	@Override
	protected void setBindings() {
		salesOrderCanBeModified = new SimpleBooleanProperty(service.salesOrderCanBeModified());
		salesReturnCanBeModified = new SimpleBooleanProperty(service.salesReturnCanBeModified());
		returnIsValid = new SimpleBooleanProperty(service.returnIsValid());
		saveButton.disableIf(when(isSalesReturn())//
				.then(salesReturnCanBeModified.not())//
				.otherwise(when(isSalesOrder())//
						.then(totalDisplay.isEmpty().or(salesOrderCanBeModified.not()))//
						.otherwise(isPosted())));
		decisionButton.disableIf(notPosted());
		bookingIdInput.disableIf((isSalesOrder().or(isBadOrder()).or(isPurchaseOrder()).or(isReturnOrder()))//
				.or(isPosted())//
				.or(isInvoice().and(noIdNo())));
		idPrefixInput.disableIf(notInvoice()//
				.or(isPosted()));
		idSuffixInput.disableIf(notInvoice()//
				.or(isPosted())//
				.or(noIdNo()));
		idNoInput.visibleProperty().bind(//
				isPurchaseOrder().not()//
						.and(isPurchaseReceipt().not()));
		idNoInput.disableIf(isPosted()//
				.or(isSalesOrder())//
				.or(isBadOrder()));
		customerIdInput.disableIf(isPosted()//
				.or((isSalesOrder().or(isBadOrder()).or(isReturnOrder())).not()));
		customerSearchButton.disableIf(customerIdInputIsDisabled());
		customerSearchButton.visibleProperty().bind(customerIdInputIsDisabled().not());
		table.disableIf(when(isSalesReturn().or(isPurchaseReceipt()))//
				.then(bookingIdInput.isEmpty())//
				.otherwise(customerNameDisplay.isEmpty()));
		decisionNeededApp.showDecisionNodesIf(notSalesReturn());
		receiptButton.disableIf(notValidReturn().or(printedOnDisplay.isEmpty())//
				.or(receivedOnDisplay.isNotEmpty()));
		invalidateButton.disableIf(notValidReturn()//
				.or(billedOnDisplay.isNotEmpty()));
		disposalButton.disableIf(notValidReturn().or(notReceived())//
				.or(modifiedReceivingOnDisplay.isNotEmpty()));
		paymentButton.disableIf(notValidReturn()
				.or(when(isBadOrder())//
						.then(modifiedReceivingOnDisplay.isEmpty())//
						.otherwise(notReceived()))//
				.or(billedOnDisplay.isNotEmpty()));
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		openByDateButton.setOnAction(e -> showOpenByDateDialog());
		invoiceBookletButton.setOnAction(e -> invoiceBookletApp.addParent(this).start());
		receiptButton.setOnAction(e -> saveItemReturnReceiptData());
		invalidateButton.setOnAction(e -> invalidateThis());
		disposalButton.setOnAction(e -> saveDisposalData());
		paymentButton.setOnAction(e -> saveInputtedItemReturnPaymentData());
		orderDatePicker.setOnAction(e -> updateUponDateValidation(orderDatePicker.getValue()));
		idSuffixInput.setOnAction(e -> updateUponOrderNoValidation());
		bookingIdInput.setOnAction(e -> updateUponBookingIdValidation(bookingIdInput.getValue()));
		table.setOnItemChange(i -> updateSummaryPane());
		decisionNeededApp.setDecisionButtonOnAction(e -> showAuditDialogToValidateOrder());
		if (isASalesOrder() || isABadOrder() || isAReturnOrder()) {
			customerIdInput.setOnAction(e -> updateUponCustomerValidation());
			customerSearchButton.setOnAction(e -> openSearchDialog());
		}
	}

	@Override
	protected HBox trackedPane() {
		if (isABill() || isABadOrder() || isAReturnOrder())
			return trackedBilledPane();
		if (isASalesReturn())
			return trackedReceivingPane();
		return super.trackedPane();
	}
}
