package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.service.ItemTreeService;

@Component("itemTreeDialog")
public class ItemTreeDialog extends FieldDialog<ItemTree> {

	@Autowired
	private LabeledCombo<ItemFamily> familyCombo;

	@Autowired
	private LabeledCombo<ItemFamily> parentCombo;

	@Autowired
	private ItemTreeService service;

	private ObservableList<ItemFamily> getParents() {
		try {
			return FXCollections.observableArrayList(service.listParents(familyCombo.getValue()));
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}

	private void setComboItems() {
		setComboItems(familyCombo.name("Family"));
		setComboItems(parentCombo.name("Parent"));
	}

	private void setComboItems(LabeledCombo<ItemFamily> combo) {
		try {
			combo.items(service.listFamilies()).build();
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
		}
	}

	private void setComboOnAction() {
		familyCombo.setOnAction(event -> parentCombo.items(getParents()));
		parentCombo.setOnAction(event -> verifyTreeIsUnique(familyCombo.getValue(), parentCombo.getValue()));
	}

	private void verifyTreeIsUnique(ItemFamily family, ItemFamily parent) {
		try {
			if (family != null && parent != null)
				service.duplicated(family, parent);
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		setComboItems();
		setComboOnAction();
		return Arrays.asList(familyCombo, parentCombo);
	}

	@Override
	protected ItemTree createEntity() {
		try {
			return service.save(familyCombo.getValue(), parentCombo.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}
}
