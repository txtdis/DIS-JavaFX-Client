package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.TEXT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ph.txtdis.dto.Keyed;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.SavedByName;
import ph.txtdis.service.UniquelyNamed;

public abstract class NameListDialog<T extends Keyed<Long>, S extends UniquelyNamed<T>> extends FieldDialog<T> {

	@Autowired
	protected LabeledField<String> nameField;

	@Autowired
	protected S service;

	private void findDuplicate(String name) throws Exception {
		if (!name.isEmpty())
			service.confirmUniqueness(name);
	}

	private void verifyNameIsUnique() {
		try {
			findDuplicate(nameField.getValue());
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
			refresh();
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		nameField.name("Name").build(TEXT);
		nameField.setOnAction(e -> verifyNameIsUnique());
		return asList(nameField);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T createEntity() {
		try {
			return ((SavedByName<T>) service).save(nameField.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}
}
