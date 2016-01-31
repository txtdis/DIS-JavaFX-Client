package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static java.math.BigDecimal.ZERO;

import ph.txtdis.app.ItemListApp;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.dto.Item;
import ph.txtdis.fx.AppSelectable;
import ph.txtdis.fx.Searchable;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.BillableService;
import ph.txtdis.service.ByNameSearchable;
import ph.txtdis.service.ItemService;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("billableDialog")
public class BillableDialog extends FieldDialog<BillableDetail> implements Searchable<Item> {

	@Autowired
	private BillableService service;

	@Autowired
	private ItemListApp itemListApp;

	@Autowired
	private ItemService itemService;

	@Autowired
	private LabeledField<Long> itemIdField;

	@Autowired
	private LabeledField<String> itemNameDisplay;

	@Autowired
	private LabeledCombo<UomType> uomCombo;

	@Autowired
	private LabeledField<BigDecimal> qtyField;

	@Autowired
	private SearchDialog searchDialog;

	@Override
	public AppSelectable<Item> getListApp() {
		return itemListApp;
	}

	@Override
	public ByNameSearchable<Item> getSearchableByNameService() {
		return itemService;
	}

	@Override
	public void nextFocus() {
		uomCombo.requestFocus();
	}

	@Override
	public void updateUponVerification(Item t) {
		itemIdField.setValue(t.getId());
		updateNameAndUomUponVerificationIfInputted();
	}

	private LabeledField<Long> itemIdField() {
		itemIdField.name("Item No.").isSearchable().build(ID);
		itemIdField.setOnAction(e -> updateNameAndUomUponVerificationIfInputted());
		itemIdField.setOnSearch(e -> openSearchDialog(this, dialog, searchDialog));
		return itemIdField;
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Quantity").build(QUANTITY);
		qtyField.setOnAction(event -> verifyQty());
		return qtyField;
	}

	private LabeledCombo<UomType> uomCombo() {
		uomCombo.name("UOM").build();
		uomCombo.setOnAction(event -> qtyField.setValue(ZERO));
		return uomCombo;
	}

	private void updateNameAndUomUponVerificationIfInputted() {
		try {
			service.setItemUponValidation(itemIdField.getValue());
			itemNameDisplay.setValue(service.getItemName());
			uomCombo.items(service.getSellingUoms());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	private void verifyQty() {
		try {
			service.setQtyUponValidation(uomCombo.getValue(), qtyField.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return asList(itemIdField(), //
				itemNameDisplay.name("Description").readOnly().build(TEXT), //
				uomCombo(), qtyField());
	}

	@Override
	protected BillableDetail createEntity() {
		return service.createDetail(uomCombo.getValue(), qtyField.getValue());
	}

	@Override
	protected String headerText() {
		return "Add New Item";
	}
}
