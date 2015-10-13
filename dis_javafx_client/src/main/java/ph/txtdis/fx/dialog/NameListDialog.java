package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ph.txtdis.dto.Keyed;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.SavedByName;
import ph.txtdis.service.UniquelyNamed;

public abstract class NameListDialog<T extends Keyed<Long>, S extends UniquelyNamed> extends FieldDialog<T> {

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
	protected void addItem() {
		verifyNameIsUnique();
		super.addItem();
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		nameField.name("Name").build(TEXT);
		nameField.setOnAction(event -> verifyNameIsUnique());
		return Arrays.asList(nameField);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T createEntity() {
		try {
			return ((SavedByName<T>) service).save(nameField.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected String headerText() {
		return "Add New " + super.headerText();
	}
}
