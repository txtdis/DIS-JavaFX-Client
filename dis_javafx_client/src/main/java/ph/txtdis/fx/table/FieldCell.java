package ph.txtdis.fx.table;

import javafx.scene.control.cell.TextFieldTableCell;
import lombok.AllArgsConstructor;
import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.type.Type;
import ph.txtdis.util.TypeStyle;

@AllArgsConstructor
@SuppressWarnings("unchecked")
public class FieldCell<S, T> extends TextFieldTableCell<S, T>implements DoubleClickable, StylableTextField {

	private Type type;

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null)
			TypeStyle.style(type, this, item);
	}
}
