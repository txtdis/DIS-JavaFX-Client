package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.TEXT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Holiday;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.HolidayService;

@Scope("prototype")
@Component("holidayDialog")
public class HolidayDialog extends FieldDialog<Holiday> {

	@Autowired
	private HolidayService service;

	@Autowired
	private LabeledDatePicker datePicker;

	@Autowired
	private LabeledField<String> nameField;

	private LabeledDatePicker datePicker() {
		datePicker.name("Date");
		datePicker.setOnAction(e -> validateDate());
		return datePicker;
	}

	private LabeledField<String> nameField() {
		return nameField.name("Name").build(TEXT);
	}

	private void validateDate() {
		try {
			service.validateDate(datePicker.getValue());
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
			refresh();
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		return asList(datePicker(), nameField());
	}

	@Override
	protected Holiday createEntity() {
		try {
			return service.save(datePicker.getValue(), nameField.getValue());
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected String headerText() {
		return "Add New Holiday";
	}
}
