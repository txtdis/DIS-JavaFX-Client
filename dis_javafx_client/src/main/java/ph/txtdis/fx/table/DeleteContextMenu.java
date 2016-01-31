package ph.txtdis.fx.table;

import static javafx.beans.binding.Bindings.when;
import static javafx.collections.FXCollections.observableArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

@Lazy
@Component("deleteContextMenu")
public final class DeleteContextMenu<S> {

	private TableView<S> table;

	private void addRowMenuItem(ContextMenu cm, MenuItem tableItem) {
		MenuItem rowItem = new MenuItem(tableItem.getText());
		rowItem.setGraphic(tableItem.getGraphic());
		rowItem.setOnAction(tableItem.getOnAction());
		cm.getItems().add(rowItem);
	}

	private void addTableMenuItemsToRowMenu(TableView<S> t, ContextMenu cm) {
		if (t != null && t.getContextMenu() != null && cm != null)
			t.getContextMenu().getItems().forEach(i -> addRowMenuItem(cm, i));
	}

	private MenuItem createDeleteRowMenuItem(TableRow<S> row) {
		MenuItem mi = new MenuItem("Remove");
		mi.setOnAction(e -> deleteSelectedItem(row));
		return mi;
	}

	private TableRow<S> createRowMenu(TableView<S> t) {
		TableRow<S> r = new TableRow<>();
		r.contextMenuProperty()
				.bind(when(r.itemProperty().isNotNull())//
						.then(createRowMenu(t, r))//
						.otherwise((ContextMenu) null));
		return r;
	}

	private ContextMenu createRowMenu(TableView<S> t, TableRow<S> r) {
		ContextMenu cm = new ContextMenu();
		addTableMenuItemsToRowMenu(t, cm);
		cm.getItems().add(createDeleteRowMenuItem(r));
		return cm;
	}

	private void deleteSelectedItem(TableRow<S> r) {
		ObservableList<S> l = observableArrayList(table.getItems());
		l.remove(r.getItem());
		table.setItems(l);
	}

	protected void addMenu(TableView<S> table) {
		this.table = table;
		table.setRowFactory(t -> createRowMenu(t));
	}
}
