package ph.txtdis.app;

import static java.time.ZonedDateTime.now;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIME;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static ph.txtdis.util.Spring.username;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import ph.txtdis.util.Spring;

@Scope("prototype")
@Component("remittanceApp")
public class RemittanceApp extends AbstractIdApp<Payment, RemittanceService, Long, Long> {

	private enum Form {
		CASH, CHECK;
	}

	private static final int WIDTH = 300;

	private static final String PROMPT = "Select date whose first entry will opened";

	@Autowired
	private AppButton openByDateButton;

	@Autowired
	private AppButton checkSearchButton;

	@Autowired
	private AppButton depositButton;

	@Autowired
	private AppButton transferButton;

	@Autowired
	private AppCombo<Form> paymentCombo;

	@Autowired
	private AppField<BigDecimal> amountInput;

	@Autowired
	private AppCombo<String> collectorCombo;

	@Autowired
	private AppField<Long> idDisplay;

	@Autowired
	private AppField<Long> checkIdInput;

	@Autowired
	private AppField<String> acctNoInput;

	@Autowired
	private AppField<Long> bankIdInput;

	@Autowired
	private AppField<String> bankDisplay;

	@Autowired
	private AppField<Long> payorIdInput;

	@Autowired
	private AppField<String> payorDisplay;

	@Autowired
	private AppField<String> receivedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> receivedOnDisplay;

	@Autowired
	private AppField<Long> depositorBankIdDisplay;

	@Autowired
	private AppField<String> depositorBankDisplay;

	@Autowired
	private AppField<LocalDate> depositDateDisplay;

	@Autowired
	private AppField<LocalTime> depositTimeDisplay;

	@Autowired
	private AppField<String> depositorDisplay;

	@Autowired
	private AppField<ZonedDateTime> depositorOnDisplay;

	@Autowired
	private LocalDatePicker paymentDatePicker;

	@Autowired
	private CheckSearchDialog checkSearchDialog;

	@Autowired
	private DepositDialog depositDialog;

	@Autowired
	private PaymentTable table;

	@Override
	public void refresh() {
		paymentDatePicker.setValue(payment().getPaymentDate());
		amountInput.setValue(payment().getValue());
		collectorCombo.items(service.getCollectorNames());
		idDisplay.setValue(service.getId());
		checkIdInput.setValue(checkId());
		acctNoInput.setValue(payment().getAccountNo());
		bankIdInput.setValue(payment().getDraweeBankId());
		bankDisplay.setValue(payment().getDraweeBank());
		paymentCombo.setValue(checkId() == null ? Form.CASH : Form.CHECK);
		payorIdInput.setValue(payment().getPayorId());
		payorDisplay.setValue(payment().getPayor());
		receivedByDisplay.setValue(payment().getReceivedBy());
		receivedOnDisplay.setValue(payment().getReceivedOn());
		depositorBankIdDisplay.setValue(payment().getDepositorBankId());
		depositorBankDisplay.setValue(payment().getDepositorBank());
		depositDateDisplay.setValue(payment().getDepositDate());
		depositTimeDisplay.setValue(payment().getDepositTime());
		depositorDisplay.setValue(payment().getDepositor());
		depositorOnDisplay.setValue(payment().getDepositorOn());
		remarksDisplay.setValue(service.getRemarks());
		table.items(service.get().getDetails());
		remarksDisplay.setValue(service.getRemarks());
		auditedApp.refresh(service);
		super.refresh();
	}

	@Override
	public void setFocus() {
		if (service.isNew())
			paymentDatePicker.requestFocus();
	}

	private BooleanBinding audited() {
		return auditedApp.isAudited();
	}

	private HBox bankBox() {
		return box.forHorizontals(bankIdInput.build(ID), bankDisplay.readOnly().width(WIDTH).build(TEXT));
	}

	private HBox basicInfoBox() {
	// @formatter:off
		return box.forGridGroup(paymentDatePicker,
			label.field("Type"), paymentCombo.items(Form.values()),
			label.field("Amount"), amountInput.build(CURRENCY),
			label.field("Received from"), collectorCombo.items(service.getCollectorNames()),
			label.field("Collection Record No."), idDisplay.readOnly().build(ID));
	// @formatter:on
	}

	private BooleanBinding cash() {
		return paymentCombo.is(Form.CASH);
	}

	private HBox checkBox() {
	// @formatter:off
		return box.forGridGroup(checkIdInput.build(ID),
			label.field("Account No."), acctNoInput.width(160).build(TEXT),
			label.field("Bank"), bankBox());
	// @formatter:on
	}

	private Long checkId() {
		return payment().getCheckId();
	}

	private HBox depositBox() {
	// @formatter:off
		return box.forGridGroup(depositedToBox(),
			label.field("on"), depositDateDisplay.readOnly().build(DATE),
			label.field("at"), depositTimeDisplay.readOnly().build(TIME),
			label.field("per"), depositorDisplay.readOnly().width(120).build(TEXT),
			label.field("on"), depositorOnDisplay.readOnly().build(TIMESTAMP));
	// @formatter:on
	}

	private BooleanBinding deposited() {
		return depositorOnDisplay.isNotEmpty();
	}

	private HBox depositedToBox() {
		return box.forHorizontals(depositorBankIdDisplay.readOnly().build(ID),
				depositorBankDisplay.readOnly().width(WIDTH).build(TEXT));
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(basicInfoBox(), 1, 0);
		gridPane.add(label.field("Check No"), 0, 1);
		gridPane.add(checkBox(), 1, 1);
		gridPane.add(label.field("Payor"), 0, 2);
		gridPane.add(transferBox(), 1, 2);
		gridPane.add(label.field("Deposited to"), 0, 3);
		gridPane.add(depositBox(), 1, 3);
		gridPane.add(label.field("Remarks"), 0, 4);
		gridPane.add(remarksDisplay.readOnly().build(TEXT), 1, 4);
		return gridPane;
	}

	private void inputDepositData() {
		depositDialog.addParent(this).start();
		if (depositDialog.getDate() != null)
			saveDeposit();
	}

	private void logTransfer() {
		payment().setReceivedBy(Spring.username());
		payment().setReceivedOn(now());
		save();
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

	private Payment payment() {
		return service.get();
	}

	private HBox payorBox() {
		return box.forHorizontals(payorIdInput.build(ID), payorDisplay.readOnly().width(WIDTH).build(TEXT));
	}

	private void saveDeposit() {
		payment().setDepositorBankId(depositDialog.getBankId());
		payment().setDepositorBank(depositDialog.getBankName());
		payment().setDepositDate(depositDialog.getDate());
		payment().setDepositTime(depositDialog.getTime());
		payment().setDepositor(username());
		payment().setDepositorOn(now());
		save();
	}

	private void setBindings() {
		saveButton.disableIf((table.isEmpty().and(remarksDisplay.isNot("CANCELLED"))).or(posted()));
		transferButton.disableIf(notPosted().or(transferred().or(deposited()).or(audited())));
		depositButton.disableIf(notPosted().or(deposited()).or(audited()));
		auditButton.disableIf(notPosted());
		paymentCombo.disableIf(noDate());
		amountInput.disableIf(noDate());
		collectorCombo.disableIf(amountInput.isEmpty());
		checkIdInput.disableIf(cash().or(collectorCombo.isEmpty()));
		acctNoInput.disableIf(cash().or(checkIdInput.isEmpty()));
		bankIdInput.disableIf(cash().or(acctNoInput.isEmpty()));
		payorIdInput.disableIf(cash().or(bankIdInput.isEmpty()));
		table.disableIf(whenCashAndNoCollectorOrWhenCheckAndNoPayor());
	}

	private void showAuditDialogToValidateOrder() {
		auditDialog.disableAcceptanceIf(deposited().not()).addParent(this).start();
		if (auditDialog.isValid() != null)
			saveAudit();
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
		dateDialog.header(h).prompt(PROMPT).addParent(this).start();
		LocalDate d = dateDialog.getDate();
		if (d != null)
			open(d);
	}

	private HBox table() {
		return box.forHorizontalPane(table.build());
	}

	private HBox transferBox() {
	// @formatter:off
		return box.forGridGroup(payorBox(),
			label.field("Fund Transfer Received by"), receivedByDisplay.readOnly().width(120).build(TEXT),
			label.field("on"), receivedOnDisplay.readOnly().build(TIMESTAMP));
	// @formatter:on
	}

	private BooleanBinding transferred() {
		return receivedOnDisplay.isNotEmpty();
	}

	private void updateUponDateValidation() {
		try {
			service.setOrderDateUponValidation(paymentDatePicker.getValue());
			refresh();
		} catch (Exception e) {
			clearControlAfterShowingErrorDialog(e, paymentDatePicker);
		}
	}

	private BooleanBinding whenCashAndNoCollectorOrWhenCheckAndNoPayor() {
		return (cash().or(collectorCombo.isEmpty())).or(paymentCombo.is(Form.CHECK).or(payorDisplay.isEmpty()));
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> b = new ArrayList<>(super.addButtons());
		b.add(2, openByDateButton.icon("openByDate").tooltip("Open a date's\nfirst entry").build());
		b.add(checkSearchButton.icon("checkSearch").tooltip("Find a check...").build());
		b.add(transferButton.icon("transfer").tooltip("Fund transfer\receipt...").build());
		b.add(depositButton.icon("deposit").tooltip("Enter deposit\ndata...").build());
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
		return Arrays.asList(gridPane(), table());
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		checkSearchButton.setOnAction(e -> showCheckSearchDialog());
		transferButton.setOnAction(e -> logTransfer());
		depositButton.setOnAction(e -> inputDepositData());
		paymentDatePicker.setOnAction(a -> updateUponDateValidation());
		openByDateButton.setOnAction(e -> showOpenByDateDialog());
		auditedApp.setOnAuditButtonClick(e -> showAuditDialogToValidateOrder());
	}
}
