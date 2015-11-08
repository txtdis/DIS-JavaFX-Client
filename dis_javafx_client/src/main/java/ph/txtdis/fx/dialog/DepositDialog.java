package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TIME;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import ph.txtdis.dto.Customer;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.RemittanceService;

@Scope("prototype")
@Component("depositDialog")
public class DepositDialog extends FieldDialog<Customer> {

	@Autowired
	private LabeledCombo<Customer> bankCombo;

	@Autowired
	private LabeledDatePicker datePicker;

	@Autowired
	private LabeledField<LocalTime> timeInput;

	@Autowired
	private RemittanceService service;

	private LocalDate date;

	private LocalTime time;

	public Long getBankId() {
		return getAddedItem().getId();
	}

	public String getBankName() {
		return getAddedItem().getName();
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	@Override
	protected Button addButton() {
		Button b = super.addButton();
		b.setText("Save");
		return b;
	}

	@Override
	protected void addItem() {
		try {
			time = timeInput.getValue();
			date = datePicker.getValue();
			super.addItem();
		} catch (DateTimeParseException e) {
			String s = "Incorrect time format;\nmust be 0808 for 8:08AM,\nand 2008 for 8:08PM.";
			dialog.showError(s).addParent(this).start();
			timeInput.setValue(null);
			timeInput.requestFocus();
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		bankCombo.name("Bank").items(service.getBanks()).build();
		datePicker.name("Date");
		timeInput.name("Time").build(TIME);
		return Arrays.asList(bankCombo, datePicker, timeInput);
	}

	@Override
	protected Customer createEntity() {
		return bankCombo.getValue();
	}

	@Override
	protected String headerText() {
		return "Enter Deposit Details";
	}
}
