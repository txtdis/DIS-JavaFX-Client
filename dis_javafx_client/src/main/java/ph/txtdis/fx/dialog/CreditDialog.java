package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CreditDetail;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.CustomerService;

@Component("creditDialog")
public class CreditDialog extends FieldDialog<CreditDetail> {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabeledField<Integer> termField;

	@Autowired
	private LabeledField<Integer> graceField;

	@Autowired
	private LabeledField<BigDecimal> creditField;

	@Autowired
	private LabeledDatePicker startDatePicker;

	private CreditDetail credit;

	private void createCreditLineUponValidation() {
		try {
			if (startDatePicker.getValue() != null)
				credit = service.createCreditLineUponValidation(termField.getValue(), graceField.getValue(),
						creditField.getValue(), startDatePicker.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private LabeledDatePicker startDatePicker() {
		startDatePicker.name("Start Date");
		startDatePicker.setOnAction(value -> createCreditLineUponValidation());
		return startDatePicker;
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		termField.name("Term Days").build(INTEGER);
		graceField.name("Grace Period Days").build(INTEGER);
		creditField.name("Credit Limit").build(CURRENCY);
		return Arrays.asList(termField, graceField, creditField, startDatePicker());
	}

	@Override
	protected CreditDetail createEntity() {
		return credit;
	}
}
