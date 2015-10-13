package ph.txtdis.fx.table;

import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.CheckBoxTableCell;

public abstract class BooleanCell<S> extends CheckBoxTableCell<S, Boolean> {
	private final CheckBox checkBox;

	public BooleanCell() {
		checkBox = new CheckBox();
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

	protected abstract void setBoolean(S item, Boolean newValue);
}
