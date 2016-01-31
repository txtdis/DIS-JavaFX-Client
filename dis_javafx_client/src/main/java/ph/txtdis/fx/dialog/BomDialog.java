package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.util.NumberUtils.isZero;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.app.ItemListApp;
import ph.txtdis.dto.Bom;
import ph.txtdis.dto.Item;
import ph.txtdis.fx.AppSelectable;
import ph.txtdis.fx.Searchable;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.ByNameSearchable;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("bomDialog")
public class BomDialog extends FieldDialog<Bom> implements Searchable<Item> {

	@Autowired
	private ItemListApp itemListApp;

	@Autowired
	private ItemService service;

	@Autowired
	private LabeledField<Long> itemIdField;

	@Autowired
	private LabeledField<String> itemNameDisplay;

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
		return service;
	}

	@Override
	public void nextFocus() {
		qtyField.requestFocus();
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

	private void nullIfZero() {
		if (isZero(qtyField.getValue()))
			qtyField.clear();
	}

	private LabeledField<BigDecimal> qtyField() {
		qtyField.name("Quantity").build(QUANTITY);
		qtyField.setOnAction(e -> nullIfZero());
		return qtyField;
	}

	private void updateNameAndUomUponVerificationIfInputted() {
		try {
			service.setPartUponValidation(itemIdField.getValue());
			itemNameDisplay.setValue(service.getItemName());
		} catch (Exception e) {
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return asList(itemIdField(), //
				itemNameDisplay.name("Description").readOnly().build(TEXT), //
				qtyField());
	}

	@Override
	protected Bom createEntity() {
		return service.createBom(qtyField.getValue());
	}

	@Override
	protected String headerText() {
		return "Add New Part";
	}
}
