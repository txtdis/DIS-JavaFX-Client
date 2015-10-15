package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.DECIMAL;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.CustomerService;

@Lazy
@Component("customerDiscountDialog")
public class CustomerDiscountDialog extends FieldDialog<CustomerDiscount> {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabeledField<Integer> levelField;

	@Autowired
	private LabeledField<BigDecimal> percentField;

	@Autowired
	private LabeledCombo<ItemFamily> familyLimitCombo;

	@Autowired
	private LabeledDatePicker startDatePicker;

	private CustomerDiscount discount;

	private void createDiscountUponValidation() {
		try {
			if (startDatePicker.getValue() != null)
				discount = service.createDiscountUponValidation(levelField.getValue(), percentField.getValue(),
						familyLimitCombo.getValue(), startDatePicker.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private List<ItemFamily> itemFamilies() {
		try {
			return service.listAllFamilies();
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	private LabeledDatePicker startDatePicker() {
		startDatePicker.name("Start Date");
		startDatePicker.setOnAction(value -> createDiscountUponValidation());
		return startDatePicker;
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		levelField.name("Level").build(INTEGER);
		percentField.name("% Discount").build(DECIMAL);
		familyLimitCombo.name("Only for").items(itemFamilies()).build();
		return Arrays.asList(levelField, percentField, familyLimitCombo, startDatePicker());
	}

	@Override
	protected CustomerDiscount createEntity() {
		return discount;
	}

	@Override
	protected String headerText() {
		return "Add New Discount";
	}
}