package ph.txtdis.fx.table;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.input.KeyCode.ENTER;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import ph.txtdis.app.Startable;
import ph.txtdis.fx.dialog.Inputted;
import ph.txtdis.service.Detailed;

@Scope("prototype")
@Component("appendContextMenu")
public final class AppendContextMenu<S> {

	private ContextMenu menu;

	private Inputted<S> dialog;

	private TableView<S> table;

	public void addItemToTable(S entity) {
		ObservableList<S> l = observableArrayList(table.getItems());
		l.add(entity);
		table.setItems(l);
	}

	public AppendContextMenu<S> addMenu(TableView<S> t, Inputted<S> d) {
		addMenu(t, d, null);
		return this;
	}

	public void addMenu(TableView<S> t, Inputted<S> d, Detailed a) {
		table = t;
		dialog = d;
		setAppendMenu(a);
	}

	public ContextMenu getContextMenu() {
		return menu;
	}

	private void addItemToTable() {
		S s = dialog.getAddedItem();
		if (s != null)
			addItemToTable(s);
	}

	private void append() {
		showAddItemDialog();
		addItemToTable();
		table.scrollTo(table.getItems().size() - 1);
	}

	private void appendIfAppendable(Detailed a) {
		if (a == null || a.isAppendable())
			append();
	}

	private MenuItem createAppendMenuItem(Detailed a) {
		MenuItem item = new MenuItem("Append");
		item.setOnAction(e -> appendIfAppendable(a));
		return item;
	}

	private ContextMenu createTableMenu(Detailed a) {
		menu = new ContextMenu();
		menu.getItems().addAll(createAppendMenuItem(a));
		return menu;
	}

	private Stage getStage() {
		Scene s = table.getScene();
		return (Stage) s.getWindow();
	}

	private void setAppendMenu(Detailed a) {
		table.setContextMenu(createTableMenu(a));
		setAppendOnPressedEnterKey(a);
	}

	private void setAppendOnPressedEnterKey(Detailed a) {
		table.setOnKeyPressed(e -> {
			if (e.getCode() == ENTER && (a == null || a.isAppendable()))
				append();
		});
	}

	private void showAddItemDialog() {
		((Startable) dialog).addParent(getStage()).start();
	}
}
