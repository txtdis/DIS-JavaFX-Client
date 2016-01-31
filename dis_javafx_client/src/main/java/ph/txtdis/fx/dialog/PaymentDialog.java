package ph.txtdis.fx.dialog;

import static ph.txtdis.type.BillingType.DELIVERY;
import static ph.txtdis.type.BillingType.INVOICE;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.util.NumberUtils.isPositive;
import static ph.txtdis.util.NumberUtils.isZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static java.math.BigDecimal.ZERO;

import javafx.beans.binding.BooleanBinding;
import ph.txtdis.dto.Billable;
import ph.txtdis.dto.PaymentDetail;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.BillableService;
import ph.txtdis.service.RemittanceService;
import ph.txtdis.type.BillingType;

@Scope("prototype")
@Component("paymentDialog")
public class PaymentDialog extends FieldDialog<PaymentDetail> {

	@Autowired
	private LabeledCombo<BillingType> billableCombo;

	@Autowired
	private LabeledField<String> prefixField;

	@Autowired
	private LabeledField<Long> idField;

	@Autowired
	private LabeledField<String> suffixField;

	@Autowired
	private LabeledField<String> customerDisplay;

	@Autowired
	private LabeledField<LocalDate> dateDueDisplay;

	@Autowired
	private LabeledField<BigDecimal> receivableDisplay;

	@Autowired
	private LabeledField<BigDecimal> paymentDisplay;

	@Autowired
	private LabeledField<BigDecimal> balanceDisplay;

	@Autowired
	private LabeledField<BigDecimal> remainingDisplay;

	@Autowired
	private RemittanceService remitService;

	@Autowired
	private BillableService billableService;

	private Billable billable;

	public Long id(BillingType b) {
		Long id = idField.getValue();
		return b.equals(BillingType.INVOICE) ? id : -id;
	}

	public String moduleId(String prefix, Long id, String suffix) {
		prefix = prefix.isEmpty() ? "" : prefix + "-";
		if (id < 0)
			return BillingType.DELIVERY + " No. " + -id;
		return BillingType.INVOICE + " No. " + prefix + id + suffix;
	}

	public void payFully(BigDecimal payment, BigDecimal remainingAfterPayment) {
		paymentDisplay.setValue(payment);
		balanceDisplay.setValue(ZERO);
		remainingDisplay.setValue(remainingAfterPayment);
	}

	public void payPartial(BigDecimal unpaidValue, BigDecimal remainingPayment) {
		paymentDisplay.setValue(remainingPayment);
		balanceDisplay.setValue(unpaidValue.subtract(remainingPayment));
		remainingDisplay.setValue(ZERO);
	}

	private LabeledField<BigDecimal> balanceDisplay() {
		return balanceDisplay.name("Balance").readOnly().build(CURRENCY);
	}

	private LabeledCombo<BillingType> billableCombo() {
		billableCombo.name("Type").items(BillingType.values()).build();
		billableCombo.select(null);
		return billableCombo;
	}

	private LabeledField<String> customerDisplay() {
		return customerDisplay.name("Customer").readOnly().build(TEXT);
	}

	private LabeledField<LocalDate> dateDueDisplay() {
		return dateDueDisplay.name("Date Due").readOnly().build(DATE);
	}

	private LabeledField<Long> idField() {
		idField.name("ID No.").build(ID);
		idField.setOnAction(e -> updateUponDeliveryIdValidation());
		return idField;
	}

	private LabeledField<BigDecimal> paymentField() {
		return paymentDisplay.name("Payment").readOnly().build(CURRENCY);
	}

	private LabeledField<String> prefixField() {
		prefixField.name("Code").width(70).build(TEXT);
		prefixField.disableIf(billableCombo.is(DELIVERY));
		return prefixField;
	}

	private LabeledField<BigDecimal> receivableDisplay() {
		return receivableDisplay.name("Amount Due").readOnly().build(CURRENCY);
	}

	private LabeledField<BigDecimal> remainingDisplay() {
		remainingDisplay.name("Remaining Payment").readOnly().build(CURRENCY);
		remainingDisplay.setValue(remitService.get().getValue());
		return remainingDisplay;
	}

	private LabeledField<String> suffixField() {
		suffixField.name("Series").width(40).build(TEXT);
		suffixField.setOnAction(e -> updateUponInvoiceIdValidation());
		suffixField.disableIf(billableCombo.is(DELIVERY));
		return suffixField;
	}

	private void updateBillableDisplays(Billable dr) {
		customerDisplay.setValue(dr.getCustomerName());
		dateDueDisplay.setValue(dr.getDueDate());
		receivableDisplay.setValue(dr.getTotalValue());
		balanceDisplay.setValue(dr.getUnpaidValue());
		updatePaymentDisplays(dr);
	}

	private void updatePaymentDisplays(Billable dr) {
		BigDecimal remainingPayment = remitService.getRemaining();
		BigDecimal unpaidValue = dr.getUnpaidValue();
		BigDecimal remainingAfterPayment = remainingPayment.subtract(unpaidValue);

		if (isPositive(remainingAfterPayment))
			payFully(unpaidValue, remainingAfterPayment);
		else
			payPartial(unpaidValue, remainingPayment);
	}

	private void updateUponDeliveryIdValidation() {
		try {
			BillingType b = billableCombo.getValue();
			if (b == null || b.equals(INVOICE))
				return;
			updateUponIdValidation(DELIVERY);
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void updateUponIdValidation(BillingType b) throws Exception {
		String prefix = prefixField.getValue();
		Long id = id(b);
		String suffix = suffixField.getValue();

		Billable i = billableService.getBillable(prefix, id, suffix);
		if (i == null)
			throw new NotFoundException(moduleId(prefix, id, suffix));
		if (remitService.foundThisBillableOnThisPaymentList(i))
			if (isZero(i.getUnpaidValue()))
				throw new Exception(moduleId(prefix, id, suffix) + "\nis fully paid");
		updateBillableDisplays(i);
		billable = i;

	}

	private void updateUponInvoiceIdValidation() {
		try {
			updateUponIdValidation(INVOICE);
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return Arrays.asList(//
				billableCombo(), //
				prefixField(), //
				idField(), //
				suffixField(), //
				customerDisplay(), //
				dateDueDisplay(), //
				receivableDisplay(), //
				paymentField(), //
				balanceDisplay(), //
				remainingDisplay());
	}

	@Override
	protected PaymentDetail createEntity() {
		return remitService.createDetail(billable, paymentDisplay.getValue(), remainingDisplay.getValue());
	}

	@Override
	protected BooleanBinding getAddButtonDisableBindings() {
		return receivableDisplay.isEmpty().or(paymentDisplay.isEmpty());
	}

	@Override
	protected String headerText() {
		return "Pay a Billable";
	}
}
