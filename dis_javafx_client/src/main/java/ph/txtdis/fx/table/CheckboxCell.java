package ph.txtdis.fx.table;

import org.apache.commons.lang3.StringUtils;

import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.CheckBoxTableCell;
import ph.txtdis.util.Reflection;

public class CheckboxCell<S> extends CheckBoxTableCell<S, Boolean> {

	private final CheckBox checkBox;
	private final String field;

	public CheckboxCell(String field) {
		this.checkBox = new CheckBox();
		this.field = field;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void updateItem(Boolean checked, boolean empty) {
		super.updateItem(checked, empty);
		if (checked != null && getTableRow() != null) {
			checkBox.setSelected(checked);
			checkBox.selectedProperty().addListener((check, oldValue, newValue) -> {
				Object item = getTableRow().getItem();
				setBoolean((S) item, newValue);
			});
			setGraphic(checkBox);
			setStyle("-fx-alignment: CENTER;");
		}
	}

	protected void setBoolean(S item, Boolean newValue) {
		Reflection.invokeOneParameterMethod(item, "set" + StringUtils.capitalize(field), newValue, boolean.class);
	}
}
