package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.ID;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import ph.txtdis.dto.Customer;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.RemittanceService;

@Lazy
@Component("checkSearchDialog")
public class CheckSearchDialog extends FieldDialog<Customer> {

	@Autowired
	private LabeledCombo<Customer> bankCombo;

	@Autowired
	private LabeledField<Long> checkIdInput;

	@Autowired
	private RemittanceService service;

	private Long checkId;

	public Customer getBank() {
		return getAddedItem();
	}

	public Long getCheckId() {
		return checkId;
	}

	@Override
	protected Button addButton() {
		Button b = super.addButton();
		b.setText("Search");
		return b;
	}

	@Override
	protected void addItem() {
		checkId = checkIdInput.getValue();
		super.addItem();
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		bankCombo.name("Bank").items(service.getBanks()).build();
		checkIdInput.name("Check No.").build(ID);
		return Arrays.asList(bankCombo, checkIdInput);
	}

	@Override
	protected Customer createEntity() {
		return bankCombo.getValue();
	}

	@Override
	protected String headerText() {
		return "Search Check";
	}
}
