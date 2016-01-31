package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.util.NumberUtils.isZero;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.BillableService;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("purchasingDialog")
public class PurchasingDialog extends FieldDialog<BillableDetail> {

	@Autowired
	private LabeledField<Long> itemIdField;

	@Autowired
	private LabeledField<String> itemNameDisplay;

	@Autowired
	private LabeledCombo<UomType> uomCombo;

	@Autowired
	private LabeledField<BigDecimal> qtyField;

	@Autowired
	private LabeledField<Integer> onPurchaseDaysLevelDisplay;

	@Autowired
	private LabeledField<Integer> onReceiptDaysLevelDisplay;

	@Autowired
	private BillableService service;

	private LabeledField<Long> itemIdField() {
		itemIdField.name("Item No.").build(ID);
		itemIdField.setOnAction(event -> updateItemBasedDataUponVerificationIfInputted());
		return itemIdField;
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Quantity").build(QUANTITY);
		qtyField.setOnAction(event -> updateOnReceiptDaysLevel());
		return qtyField;
	}

	private void updateItemBasedDataUponVerification(Long id) {
		try {
			service.setItemUponValidation(id);
			itemNameDisplay.setValue(service.getItemName());
			uomCombo.items(service.getBuyingUoms());
			onPurchaseDaysLevelDisplay.setValue(service.getOnPurchaseDaysLevel());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void updateItemBasedDataUponVerificationIfInputted() {
		Long id = itemIdField.getValue();
		if (id != 0)
			updateItemBasedDataUponVerification(id);
	}

	private void updateOnReceiptDaysLevel() {
		UomType uom = uomCombo.getValue();
		BigDecimal qty = qtyField.getValue();
		if (!isZero(qty)) {
			try {
				onReceiptDaysLevelDisplay.setValue(service.getOnReceiptDaysLevel(uom, qty));
			} catch (Exception e) {
				resetNodesOnError(e);
			}
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return Arrays.asList(itemIdField(), //
				itemNameDisplay.name("Description").readOnly().build(TEXT), //
				uomCombo.name("UOM").build(), //
				onPurchaseDaysLevelDisplay.name("On-Purchase Days Level").readOnly().build(INTEGER), //
				qtyField(), //
				onReceiptDaysLevelDisplay.name("On-Receipt Days Level").readOnly().build(INTEGER));
	}

	@Override
	protected BillableDetail createEntity() {
		return service.createDetail(uomCombo.getValue(), qtyField.getValue());
	}

	@Override
	protected BooleanBinding getAddButtonDisableBindings() {
		return itemNameDisplay.isEmpty().or(uomCombo.isEmpty()).or(qtyField.isEmpty());
	}

	@Override
	protected String headerText() {
		return "Add New Item";
	}
}
