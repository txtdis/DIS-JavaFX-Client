package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.util.NumberUtils.isZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCheckBox;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.ItemService;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("qtyPerUomDialog")
public class QtyPerUomDialog extends FieldDialog<QtyPerUom> {

	@Autowired
	private ItemService service;

	@Autowired
	private LabeledCombo<UomType> uomCombo;

	@Autowired
	private LabeledField<BigDecimal> qtyField;

	@Autowired
	private LabeledCheckBox isPurchasedCheckBox, isSoldCheckBox, isReportedCheckBox;

	private LabeledCheckBox isPurchasedCheckBox() {
		isPurchasedCheckBox.name("Purchased").build();
		isPurchasedCheckBox.setOnAction(e -> verifyPurchasedUom());
		return isPurchasedCheckBox;
	}

	private LabeledCheckBox isReportedCheckBox() {
		isReportedCheckBox.name("Reported").build();
		isReportedCheckBox.setOnAction(e -> verifyReportedUom());
		return isReportedCheckBox;
	}

	private Boolean nullIfFalse(boolean b) {
		return b ? b : null;
	}

	private void nullIfZero() {
		if (isZero(qtyField.getValue()))
			qtyField.clear();
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Qty Relative to 'PC'").build(QUANTITY);
		qtyField.setOnAction(e -> nullIfZero());
		return qtyField;
	}

	private void verifyPurchasedUom() {
		try {
			service.validatePurchasedUom(isPurchasedCheckBox.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void verifyReportedUom() {
		try {
			service.validateReportedUom(isReportedCheckBox.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		List<InputNode<?>> l = new ArrayList<>(asList(//
				uomCombo.name("UOM").items(service.listUoms()).build(), //
				qtyField(), //
				isSoldCheckBox.name("Sold").build(), //
				isReportedCheckBox()));
		if (service.isPurchased())
			l.add(2, isPurchasedCheckBox());
		return l;
	}

	@Override
	protected QtyPerUom createEntity() {
		return service.createQtyPerUom(//
				uomCombo.getValue(), //
				qtyField.getValue(), //
				nullIfFalse(isPurchasedCheckBox.getValue()), //
				nullIfFalse(isSoldCheckBox.getValue()), //
				nullIfFalse(isReportedCheckBox.getValue()));
	}

	@Override
	protected String headerText() {
		return "Add New Qty per UOM";
	}
}
