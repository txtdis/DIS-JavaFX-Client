package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.service.ItemFamilyService;
import ph.txtdis.type.ItemTier;

@Component("itemFamilyDialog")
public class ItemFamilyDialog extends NameListDialog<ItemFamily, ItemFamilyService> {

	@Autowired
	private LabeledCombo<ItemTier> tierCombo;

	@Override
	protected List<InputNode<?>> addNodes() {
		super.addNodes();
		tierCombo.name("Tier").items(ItemTier.values()).build();
		return Arrays.asList(nameField, tierCombo);
	}

	@Override
	protected ItemFamily createEntity() {
		try {
			return service.save(nameField.getValue(), tierCombo.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}
}
