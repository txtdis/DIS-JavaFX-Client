package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;
import static javafx.beans.binding.Bindings.when;
import static ph.txtdis.type.ItemType.BUNDLED;
import static ph.txtdis.type.ItemType.FREE;
import static ph.txtdis.type.ItemType.PROMO;
import static ph.txtdis.type.ItemType.PURCHASED;
import static ph.txtdis.type.ItemType.REPACKED;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.control.AppCheckBox;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.LocalDatePicker;
import ph.txtdis.fx.table.BomTable;
import ph.txtdis.fx.table.QtyPerUomTable;
import ph.txtdis.service.ItemService;
import ph.txtdis.type.ItemType;

@Scope("prototype")
@Component("itemTab")
public class ItemTab extends AbstractTab {

	@Autowired
	private ItemService service;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<Long> idDisplay;

	@Autowired
	private AppField<String> nameField;

	@Autowired
	private AppField<String> descriptionField;

	@Autowired
	private AppField<String> vendorIdField;

	@Autowired
	private AppCombo<ItemType> typeCombo;

	@Autowired
	private AppCombo<ItemFamily> familyCombo;

	@Autowired
	private BomTable bomTable;

	@Autowired
	private LocalDatePicker endOfLifePicker;

	@Autowired
	private QtyPerUomTable qtyPerUomTable;

	@Autowired
	private AppCheckBox notDiscountedCheckbox;

	private BooleanProperty hasNeededPurchaseUom, hasNeededReportUom, hasVendorId;

	public ItemTab() {
		super("Basic Information");
	}

	public BooleanBinding hasIncompleteData() {
		return doesNotHaveNeededUoms() //
				.or(bomTable.isEmpty()//
						.and(typeCombo.are(BUNDLED, PROMO, REPACKED)));
	}

	public BooleanBinding needsPrice() {
		return typeCombo.isNot(FREE);
	}

	@Override
	public void refresh() {
		idDisplay.setValue(service.getId());
		nameField.setValue(item().getName());
		descriptionField.setValue(item().getDescription());
		typeCombo.select(item().getType());
		familyCombo.items(service.listParents());
		vendorIdField.setValue(vendorId());
		hasVendorId.set(vendorId() != null);
		refreshEndOfLifePicker();
		notDiscountedCheckbox.setValue(item().isNotDiscounted());
		qtyPerUomTable.items(service.listQtyPerUom());
		bomTable.items(service.listBoms());
	}

	@Override
	public void save() {
		item().setVendorId(vendorIdField.getValue());
		item().setEndOfLife(endOfLifePicker.getValue());
		item().setType(typeCombo.getValue());
		updateBom();
		if (!isNew())
			return;
		item().setName(nameField.getText());
		item().setDescription(descriptionField.getText());
		setTypeAfterClearingTypeDependentControls();
		item().setFamily(familyCombo.getValue());
		item().setNotDiscounted(notDiscountedCheckbox.getValue());
		updateQtyPerUom();
	}

	@Override
	public void select() {
		super.select();
		if (isNew())
			nameField.requestFocus();
	}

	private VBox bomTablePane() {
		return box.forVerticals(label.group("Bill of Materials"), bomTable.build());
	}

	private BooleanBinding doesNotHaveNeededUoms() {
		return hasNeededReportUom.not()//
				.or(isPurchased().and(hasNeededPurchaseUom.not()));
	}

	private BooleanBinding isFree() {
		return typeCombo.is(FREE);
	}

	private boolean isNew() {
		return service.isNew();
	}

	private BooleanBinding isPurchased() {
		return typeCombo.is(PURCHASED);
	}

	private Item item() {
		return service.get();
	}

	private BooleanBinding posted() {
		return idDisplay.isNotEmpty();
	}

	private VBox qtyPerUomTablePane() {
		return box.forVerticals(label.group("Qty per UOM Relative to 'PC'"), qtyPerUomTable.build());
	}

	private void refreshEndOfLifePicker() {
		LocalDate eol = item().getEndOfLife();
		setEndOfLifePrompt(eol);
		endOfLifePicker.setValue(eol);
	}

	private void setEndOfLifePrompt(LocalDate eol) {
		if (!isNew() && eol == null)
			endOfLifePicker.setPromptText(null);
		else
			endOfLifePicker.setPromptText("08/08/2008");
	}

	private void setTypeAfterClearingTypeDependentControls() {
		if (isNew()) {
			vendorIdField.setValue(null);
			notDiscountedCheckbox.setValue(null);
			qtyPerUomTable.items(null);
			bomTable.items(null);
			item().setType(typeCombo.getValue());
		}
	}

	private HBox tableBox() {
		return box.forHorizontalPane(qtyPerUomTablePane(), bomTablePane());
	}

	private void updateBom() {
		item().setBoms(bomTable.getItems());
	}

	private void updateQtyPerUom() {
		item().setQtyPerUomList(qtyPerUomTable.getItems());
		hasNeededPurchaseUom.set(service.hasPurchaseUom());
		hasNeededReportUom.set(service.hasReportUom());
	}

	private void validateName() {
		if (isNew())
			try {
				service.setNameIfUnique(nameField.getValue());
			} catch (Exception e) {
				handleError(nameField, e);
			}
	}

	private String vendorId() {
		return item().getVendorId();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		try {
			gridPane.getChildren().clear();
			gridPane.add(label.field("ID No."), 0, 0);
			gridPane.add(idDisplay.readOnly().build(ID), 1, 0);
			gridPane.add(label.name("Name"), 2, 0);
			gridPane.add(nameField.length(18).width(180).build(TEXT), 3, 0, 2, 1);
			gridPane.add(label.field("Description"), 5, 0);
			gridPane.add(descriptionField.width(320).build(TEXT), 6, 0, 4, 1);

			gridPane.add(label.field("Type"), 0, 1);
			gridPane.add(typeCombo.items(service.listTypes()), 1, 1, 2, 1);
			gridPane.add(label.field("Family"), 3, 1);
			gridPane.add(familyCombo, 4, 1);
			gridPane.add(label.field("Vendor ID"), 5, 1);
			gridPane.add(vendorIdField.build(TEXT).width(180), 6, 1);
			gridPane.add(label.field("End of Life"), 7, 1);
			gridPane.add(endOfLifePicker, 8, 1);
			gridPane.add(notDiscountedCheckbox.label("Customer Discount NOT Applied"), 9, 1);
			return asList(gridPane, tableBox());
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
			return null;
		}
	}

	@Override
	protected void setBindings() {
		hasNeededPurchaseUom = new SimpleBooleanProperty(false);
		hasNeededReportUom = new SimpleBooleanProperty(false);
		hasVendorId = new SimpleBooleanProperty(false);
		nameField.disableIf(posted());
		descriptionField.disableIf(nameField.isEmpty()//
				.or(posted()));
		vendorIdField.disableIf(familyCombo.isEmpty()//
				.or(isPurchased().not())//
				.or(posted().and(hasVendorId)));
		endOfLifePicker.disableIf(isPurchased().and(vendorIdField.isEmpty()));
		notDiscountedCheckbox.disableIf(endOfLifePicker.disabledProperty()//
				.or(isFree()));
		qtyPerUomTable.disableIf(when(isFree())//
				.then(endOfLifePicker.disabledProperty())//
				.otherwise(notDiscountedCheckbox.disabledProperty()));
		bomTable.disableIf(doesNotHaveNeededUoms()//
				.or(isPurchased()).or(isFree()));
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		nameField.setOnAction(e -> validateName());
		typeCombo.setOnAction(e -> setTypeAfterClearingTypeDependentControls());
		bomTable.setOnItemChange(e -> updateBom());
		qtyPerUomTable.setOnItemChange(e -> updateQtyPerUom());
	}
}
