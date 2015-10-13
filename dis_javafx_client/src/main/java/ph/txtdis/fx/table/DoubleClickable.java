package ph.txtdis.fx.table;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

public interface DoubleClickable {

	default void setOnMouseClick(EventHandler<MouseEvent> event) {
		setEditable(false);
		addEventFilter(MouseEvent.MOUSE_CLICKED, event);
	}

	void setEditable(boolean value);

	<T extends Event> void addEventFilter(final EventType<T> eventType, final EventHandler<? super T> eventFilter);

	TableRow<?> getTableRow();
}
