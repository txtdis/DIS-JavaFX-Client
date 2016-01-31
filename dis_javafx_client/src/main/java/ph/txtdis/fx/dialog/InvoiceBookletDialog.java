package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.ALPHA;
import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.ID;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.InvoiceBookletService;

@Scope("prototype")
@Component("invoiceBookletDialog")
public class InvoiceBookletDialog extends FieldDialog<InvoiceBooklet> {

	@Autowired
	private LabeledField<String> prefixField;

	@Autowired
	private LabeledField<String> suffixField;

	@Autowired
	private LabeledField<Long> startIdField;

	@Autowired
	private LabeledField<Long> endIdField;

	@Autowired
	private LabeledCombo<String> issuedToCombo;

	@Autowired
	protected InvoiceBookletService service;

	private void setInputs() {
		prefixField.name("Code").width(70).build(CODE);
		suffixField.name("Series").width(40).build(ALPHA);
		startIdField.name("First No.").build(ID);
		endIdField.name("Last No.").build(ID);
		issuedToCombo.name("IssuedTo");
	}

	private void setOnAction() {
		startIdField.setOnAction(e -> verifyIdIsUnissued(startIdField.getValue()));
		endIdField.setOnAction(event -> verifyIdIsUnissued(endIdField.getValue()));
	}

	private void updateIssuedToCombo() {
		try {
			issuedToCombo.items(service.listUsers()).build();
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void verifyIdIsUnissued(Long id) {
		try {
			service.checkForDuplicates(prefixField.getValue(), id, suffixField.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		setInputs();
		setOnAction();
		updateIssuedToCombo();
		return Arrays.asList(prefixField, suffixField, startIdField, endIdField, issuedToCombo);
	}

	@Override
	protected InvoiceBooklet createEntity() {
		try {
			return service.save(prefixField.getValue(), suffixField.getValue(), startIdField.getValue(),
					endIdField.getValue(), issuedToCombo.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected BooleanBinding getAddButtonDisableBindings() {
		return startIdField.isEmpty().or(endIdField.isEmpty()).or(issuedToCombo.isEmpty());
	}

	@Override
	protected String headerText() {
		return "Add New Invoice Booklet";
	}
}
