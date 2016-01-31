package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.BillableService;

@Scope("prototype")
@Component("returnPaymentDialog")
public class ReturnPaymentDialog extends FieldDialog<LocalDate> {

	@Autowired
	private LabeledField<String> prefixField;

	@Autowired
	private LabeledField<Long> idField;

	@Autowired
	private LabeledField<String> suffixField;

	@Autowired
	private LabeledDatePicker datePicker;

	@Autowired
	private BillableService service;

	private LabeledField<String> suffixField() {
		suffixField.name("Series").width(40).build(TEXT);
		suffixField.setOnAction(e -> updateUponInvoiceIdValidation());
		return suffixField;
	}

	private void updateUponInvoiceIdValidation() {
		try {
			service.updateUponOrderNoValidation(prefixField.getValue(), idField.getValue(), suffixField.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return Arrays.asList(//
				prefixField.name("Code").width(70).build(TEXT), //
				idField.name("ID No.").build(ID), //
				suffixField(), //
				datePicker.name("Date")); //
	}

	@Override
	protected LocalDate createEntity() {
		return datePicker.getValue();
	}

	@Override
	protected BooleanBinding getAddButtonDisableBindings() {
		return idField.isEmpty().or(datePicker.isEmpty());
	}

	@Override
	protected String headerText() {
		return "Pay the B/O";
	}
}
