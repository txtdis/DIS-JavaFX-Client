package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.DECIMAL;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.AbstractSoldOrder;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.SoldService;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Lazy
@Component("soldOrderDialog")
public class SoldOrderDialog extends FieldDialog<BillableDetail> {

	@Autowired
	private LabeledField<Long> itemIdField;

	@Autowired
	private LabeledField<String> itemNameDisplay;

	@Autowired
	private LabeledCombo<UomType> uomCombo;

	@Autowired
	private LabeledField<BigDecimal> qtyField;

	@Autowired
	private LabeledCombo<QualityType> qualityCombo;

	private SoldService<? extends AbstractSoldOrder<Long>, Long> service;

	public SoldOrderDialog addService(SoldService<? extends AbstractSoldOrder<Long>, Long> service) {
		this.service = service;
		return this;
	}

	private LabeledField<Long> itemIdField() {
		itemIdField.name("Item No.").build(ID);
		itemIdField.setOnAction(event -> updateNameAndUomUponVerificationIfInputted());
		return itemIdField;
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Quantity").build(DECIMAL);
		qtyField.setOnAction(event -> qtyField.getValue());
		return qtyField;
	}

	private void updateNameAndUomUponVerification(Long id) {
		try {
			service.setItemUponValidation(id);
			itemNameDisplay.setValue(service.getItemDescription());
			uomCombo.items(service.getSellingUoms());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void updateNameAndUomUponVerificationIfInputted() {
		Long id = itemIdField.getValue();
		if (id != 0)
			updateNameAndUomUponVerification(id);
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		itemNameDisplay.name("Description").readOnly().build(TEXT);
		uomCombo.name("UOM").items(UomType.values()).build();
		qualityCombo.name("Quality").items(QualityType.values()).build();
		return Arrays.asList(itemIdField(), itemNameDisplay, uomCombo, qtyField(), qualityCombo);
	}

	@Override
	protected BillableDetail createEntity() {
		return service.createDetail(uomCombo.getValue(), qtyField.getValue(), qualityCombo.getValue());
	}

	@Override
	protected String headerText() {
		return "Add New Item";
	}
}
