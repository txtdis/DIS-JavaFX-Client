package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.QUANTITY;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.BillableDetail;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.BillableService;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("receivingDialog")
public class ReceivingDialog extends FieldDialog<BillableDetail> {

	@Autowired
	private LabeledCombo<String> itemCombo;

	@Autowired
	private LabeledField<UomType> uomDisplay;

	@Autowired
	private LabeledField<BigDecimal> qtyField;

	@Autowired
	private BillableService service;

	@Override
	public void refresh() {
		super.refresh();
		itemCombo.items(service.getReceivableItemNames());
	}

	private LabeledCombo<String> itemCombo() {
		itemCombo.name("Item").build();
		itemCombo.setOnAction(event -> setItemAndUom());
		return itemCombo;
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Quantity").build(QUANTITY);
		qtyField.setOnAction(event -> qtyField.getValue());
		return qtyField;
	}

	private void setItemAndUom() {
		String i = itemCombo.getValue();
		if (i != null) {
			UomType u = service.getUomOfSelectedItem(i);
			uomDisplay.setValue(u);
		}
	}

	private LabeledField<UomType> uomDisplay() {
		return uomDisplay.name("UOM").readOnly().build(ENUM);
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return asList(itemCombo(), uomDisplay(), qtyField());
	}

	@Override
	protected BillableDetail createEntity() {
		return service.updateReceivingDetailReturnedQty(qtyField.getValue());
	}

	@Override
	protected String headerText() {
		return "Receive an Item";
	}
}
