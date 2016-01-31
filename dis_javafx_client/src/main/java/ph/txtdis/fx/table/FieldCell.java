package ph.txtdis.fx.table;

import static ph.txtdis.util.TypeStyle.style;

import javafx.scene.control.cell.TextFieldTableCell;
import lombok.AllArgsConstructor;
import ph.txtdis.fx.control.StylableTextField;
import ph.txtdis.type.Type;

@AllArgsConstructor
@SuppressWarnings("unchecked")
public class FieldCell<S, T> extends TextFieldTableCell<S, T> implements DoubleClickable, StylableTextField {

	private Type type;

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null)
			style(type, this, item);
	}
}
