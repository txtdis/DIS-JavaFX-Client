package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.QUANTITY;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static ph.txtdis.type.QualityType.values;

import ph.txtdis.app.ItemListApp;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.StockTakeDetail;
import ph.txtdis.fx.AppSelectable;
import ph.txtdis.fx.Searchable;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.ByNameSearchable;
import ph.txtdis.service.ItemService;
import ph.txtdis.service.StockTakeService;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Scope("prototype")
@Component("stockTakeDialog")
public class StockTakeDialog extends FieldDialog<StockTakeDetail> implements Searchable<Item> {

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
	private LabeledCombo<QualityType> qualityCombo;

	@Autowired
	private StockTakeService service;

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
		itemIdField.setOnAction(event -> updateNameAndUomUponVerificationIfInputted());
		return itemIdField;
	}

	private void updateNameAndUomUponVerification(Long id) {
		try {
			service.setItemUponValidation(id);
			itemNameDisplay.setValue(service.getItemName());
			uomCombo.items(service.getSellingUoms());
			qualityCombo.items(asList(values()));
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
		return Arrays.asList(itemIdField(), //
				itemNameDisplay.name("Description").readOnly().build(TEXT), //
				uomCombo.name("UOM").build(), //
				qtyField.name("Quantity").build(QUANTITY), //
				qualityCombo.name("Quality").build());
	}

	@Override
	protected StockTakeDetail createEntity() {
		return service.createDetail(uomCombo.getValue(), qtyField.getValue(), qualityCombo.getValue());
	}

	@Override
	protected String headerText() {
		return "Add New Item";
	}
}
