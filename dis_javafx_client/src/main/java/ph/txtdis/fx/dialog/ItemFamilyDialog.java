package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static ph.txtdis.type.ItemTier.values;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.service.ItemFamilyService;
import ph.txtdis.type.ItemTier;

@Scope("prototype")
@Component("itemFamilyDialog")
public class ItemFamilyDialog extends NameListDialog<ItemFamily, ItemFamilyService> {

	@Autowired
	private LabeledCombo<ItemTier> tierCombo;

	@Override
	protected List<InputNode<?>> addNodes() {
		super.addNodes();
		tierCombo.name("Tier").items(values()).build();
		return asList(nameField, tierCombo);
	}

	@Override
	protected ItemFamily createEntity() {
		try {
			return service.save(nameField.getValue(), tierCombo.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected String headerText() {
		return "Add New Item Family";
	}
}
