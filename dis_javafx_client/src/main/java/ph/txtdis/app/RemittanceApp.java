package ph.txtdis.app;

import static ph.txtdis.type.PaymentType.CASH;
import static ph.txtdis.type.PaymentType.CHECK;
import static ph.txtdis.type.PaymentType.values;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Payment;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.dialog.CheckSearchDialog;
import ph.txtdis.fx.dialog.DepositDialog;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.PaymentTable;
import ph.txtdis.service.RemittanceService;
import ph.txtdis.type.PaymentType;

@Scope("prototype")
@Component("remittanceApp")
public class RemittanceApp extends AbstractIdApp<RemittanceService, Long, Long> {

	private static final String PROMPT = "Select date whose first entry will opened";

	@Autowired
	private AppButton checkSearchButton, depositButton, historyButton, openByDateButton, transferButton;

	@Autowired
	private AppCombo<PaymentType> paymentCombo;

	@Autowired
	private AppCombo<String> collectorCombo;

	@Autowired
	private AppCombo<Customer> draweeBankCombo;

	@Autowired
	private AppField<Long> idDisplay, checkIdInput;

	@Autowired
	private AppField<BigDecimal> amountInput;

	@Autowired
	private AppField<Customer> depositorBankDisplay;

	@Autowired
	private AppField<String> depositorDisplay, receivedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> receivedOnDisplay, depositedOnDisplay, depositorOnDisplay;

	@Autowired
	private LocalDatePicker paymentDatePicker;

	@Autowired
	private CheckSearchDialog checkSearchDialog;

	@Autowired
	private DepositDialog depositDialog;

	@Autowired
	private PaymentHistoryApp historyApp;

	@Autowired
	private PaymentTable table;

	private BooleanProperty userAllowedToMakeCashDeposits, userAllowedToMakeCheckDeposits,
			userAllowedToPostCollectionData, userAllowedToReceiveFundTransfer;

	@Override
	public void refresh() {
		super.refresh();
		paymentDatePicker.setValue(payment().getPaymentDate());
		amountInput.setValue(payment().getValue());
		refreshInputsAfterPaymentInput();
		idDisplay.setValue(service.getId());
		receivedByDisplay.setValue(payment().getReceivedBy());
		receivedOnDisplay.setValue(payment().getReceivedOn());
		depositorBankDisplay.setValue(payment().getDepositorBank());
		depositedOnDisplay.setValue(payment().getDepositedOn());
		depositorDisplay.setValue(payment().getDepositor());
		depositorOnDisplay.setValue(payment().getDepositorOn());
		remarksDisplay.setValue(service.getRemarks());
		decisionNeededApp.refresh(service);
	}

	@Override
	public void setFocus() {
		if (service.isNew())
			paymentDatePicker.requestFocus();
	}

	private AppButton auditButton() {
		return decisionButton = decisionNeededApp.addDecisionButton();
	}

	private BooleanBinding audited() {
		return decisionNeededApp.isAudited();
	}

	private HBox basicInfoBox() {
		return box.forGridGroup(paymentDatePicker, //
				label.field("Amount"), amountInput.build(CURRENCY), //
				label.field("Received from"), collectorCombo.width(180).items(service.getCollectorNames()), //
				label.field("Collection Record No."), idDisplay.readOnly().build(ID));
	}

	private BooleanBinding cash() {
		return paymentCombo.is(CASH);
	}

	private HBox checkBox() {
		return box.forGridGroup(//
				paymentCombo.items(values()), //
				label.field("Check No."), checkIdInput.build(ID), //
				label.field("Bank"), draweeBankCombo.items(service.getBanks()));
	}

	private Long checkId() {
		return payment().getCheckId();
	}

	private void clearNodesAfterPaymentComboAndValidateNoCashCollectionHasBeenReceivedFromCollector() {
		try {
			if (paymentCombo.getValue() == CASH)
				service.validateCashCollection();
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, paymentCombo);
		} finally {
			service.resetInputDataRelatedToPayment();
			refreshInputsAfterPaymentCombo();
		}
	}

	private void clearPaymentInputSucceedingNodes() {
		service.resetInputDataRelatedToPayment();
		refreshInputsAfterPaymentInput();
	}

	private HBox depositBox() {
		return box.forGridGroup(depositorBankDisplay.readOnly().width(240).build(OTHERS), //
				label.field("on"), depositedOnDisplay.readOnly().build(TIMESTAMP), //
				label.field("per"), depositorDisplay.readOnly().width(120).build(TEXT), //
				label.field("on"), depositorOnDisplay.readOnly().build(TIMESTAMP));
	}

	private BooleanBinding deposited() {
		return depositorOnDisplay.isNotEmpty();
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(basicInfoBox(), 1, 0);
		gridPane.add(label.field("Type"), 0, 1);
		gridPane.add(checkBox(), 1, 1);
		gridPane.add(label.field("Fund Transfer"), 0, 2);
		gridPane.add(transferBox(), 1, 2);
		gridPane.add(label.field("Deposited to"), 0, 3);
		gridPane.add(depositBox(), 1, 3);
		gridPane.add(label.field("Remarks"), 0, 4);
		gridPane.add(remarksDisplay.readOnly().build(TEXT), 1, 4);
		return gridPane;
	}

	private void inputDepositData() {
		depositDialog.addParent(this).start();
		if (depositDialog.getTimestamp() != null)
			setDepositData();
	}

	private BooleanBinding noDate() {
		return paymentDatePicker.isEmpty();
	}

	private void open(Customer bank, Long checkId) {
		try {
			service.open(bank, checkId);
			refresh();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private void openHistoryApp() {
		historyApp.addParent(this).start();
		Long id = historyApp.getSelectedId();
		if (id != null)
			actOn(id.toString());
	}

	private Payment payment() {
		return service.get();
	}

	private void refreshInputsAfterPaymentCombo() {
		table.items(service.get().getDetails());
		checkIdInput.setValue(checkId());
		draweeBankCombo.items(service.getDraweeBank());
	}

	private void refreshInputsAfterPaymentInput() {
		collectorCombo.items(service.getCollectorNames());
		paymentCombo.items(service.getPaymentTypes());
		refreshInputsAfterPaymentCombo();
	}

	private void setDepositData() {
		service.setDepositData(depositDialog.getBank(), depositDialog.getTimestamp());
		save();
	}

	private void setFundTransferData() {
		service.setFundTransferData();
		save();
	}

	private void setPayment() {
		service.setPayment(amountInput.getValue());
		clearPaymentInputSucceedingNodes();
	}

	private void showAuditDialogToValidateOrder() {
		decisionNeededApp.showDecisionDialogForValidation(this, service);
		refresh();
	}

	private void showCheckSearchDialog() {
		checkSearchDialog.addParent(this).start();
		Customer bank = checkSearchDialog.getBank();
		Long checkId = checkSearchDialog.getCheckId();
		if (bank != null)
			open(bank, checkId);
	}

	private void showOpenByDateDialog() {
		String h = service.getOpenDialogHeading();
		openByDateDialog.header(h).prompt(PROMPT).addParent(this).start();
		LocalDate d = openByDateDialog.getDate();
		if (d != null)
			open(d);
	}

	private HBox table() {
		return box.forHorizontalPane(table.build());
	}

	private HBox transferBox() {
		return box.forGridGroup(//
				label.field("Received by"), //
				receivedByDisplay.readOnly().width(120).build(TEXT), //
				label.field("on"), //
				receivedOnDisplay.readOnly().build(TIMESTAMP));
	}

	private BooleanBinding transferred() {
		return receivedOnDisplay.isNotEmpty();
	}

	private void updateUponCheckAndBankValidation() {
		if (table.isEmpty().get() && !checkIdInput.isEmpty().get()) {
			try {
				service.validateBankCheckBeforeSetting(draweeBankCombo.getValue());
			} catch (Exception e) {
				showErrorDialog(e);
				checkIdInput.setValue(null);
				draweeBankCombo.items(service.getBanks());
				checkIdInput.requestFocus();
			}
		}
	}

	private void updateUponDateValidation() {
		try {
			service.validateOrderDateBeforeSetting(paymentDatePicker.getValue());
		} catch (Exception e) {
			showErrorDialog(e);
		} finally {
			refresh();
			paymentDatePicker.requestFocus();
		}
	}

	private BooleanBinding whenUnknownPaymentTypeOrWhenCheckWithoutDraweeBank() {
		return (paymentCombo.disabledProperty()//
				.or(cash().not().and(draweeBankCombo.disabledProperty())));
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> b = new ArrayList<>(super.addButtons());
		b.add(historyButton.icon("list").tooltip("Show collection records\nof the last 15 days").build());
		b.add(2, openByDateButton.icon("openByDate").tooltip("Open a date's\nfirst entry").build());
		b.add(checkSearchButton.icon("checkSearch").tooltip("Find a check").build());
		b.add(transferButton.icon("transfer").tooltip("Fund transfer\receipt").build());
		b.add(depositButton.icon("deposit").tooltip("Enter deposit\ndata").build());
		b.add(auditButton());
		return b;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return Arrays.asList(gridPane(), table(), trackedPane());
	}

	@Override
	protected void setBindings() {
		userAllowedToMakeCashDeposits = new SimpleBooleanProperty(service.userAllowedToMakeCashDeposits());
		userAllowedToMakeCheckDeposits = new SimpleBooleanProperty(service.userAllowedToMakeCheckDeposits());
		userAllowedToPostCollectionData = new SimpleBooleanProperty(service.userAllowedToPostCollectionData());
		userAllowedToReceiveFundTransfer = new SimpleBooleanProperty(service.userAllowedToReceiveFundTransfer());
		saveButton.disableIf(isPosted()//
				.or(table.isEmpty().and(remarksDisplay.isNot("CANCELLED")))//
				.or(userAllowedToPostCollectionData.not()));
		transferButton.disableIf(notPosted()//
				.or(transferred())//
				.or(deposited())//
				.or(audited())//
				.or(userAllowedToReceiveFundTransfer.not()));
		depositButton.disableIf(notPosted()//
				.or(deposited())//
				.or(audited())//
				.or(paymentCombo.is(CASH).and(userAllowedToMakeCashDeposits.not()))//
				.or(paymentCombo.is(CHECK).and(userAllowedToMakeCheckDeposits.not())));
		decisionButton.disableIf(notPosted());
		amountInput.disableIf(noDate().or(paymentCombo.isEmpty().not()));
		collectorCombo.disableIf(amountInput.isEmpty());
		paymentCombo.disableIf(collectorCombo.disabledProperty());
		checkIdInput.disableIf(cash());
		draweeBankCombo.disableIf(cash()//
				.or(checkIdInput.isEmpty()));
		table.disableIf(whenUnknownPaymentTypeOrWhenCheckWithoutDraweeBank());
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		checkSearchButton.setOnAction(e -> showCheckSearchDialog());
		historyButton.setOnAction(e -> openHistoryApp());
		transferButton.setOnAction(e -> setFundTransferData());
		openByDateButton.setOnAction(e -> showOpenByDateDialog());
		decisionNeededApp.setDecisionButtonOnAction(e -> showAuditDialogToValidateOrder());
		amountInput.setOnAction(e -> setPayment());
		collectorCombo.setOnAction(e -> service.setCollector(collectorCombo.getValue()));
		checkIdInput.setOnAction(e -> service.setCheckId(checkIdInput.getValue()));
		draweeBankCombo.setOnAction(e -> updateUponCheckAndBankValidation());
		depositButton.setOnAction(e -> inputDepositData());
		paymentDatePicker.setOnAction(a -> updateUponDateValidation());
		paymentCombo
				.setOnAction(e -> clearNodesAfterPaymentComboAndValidateNoCashCollectionHasBeenReceivedFromCollector());
	}

	@Override
	protected HBox trackedPane() {
		HBox b = super.trackedPane();
		List<Node> l = new ArrayList<>(b.getChildren());
		l.addAll(decisionNeededApp.addAuditNodes());
		b.getChildren().setAll(l);
		return b;
	}
}
