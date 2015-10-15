package ph.txtdis.fx.table;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ph.txtdis.app.Startable;
import ph.txtdis.fx.dialog.Inputted;

@Lazy
@Component("appendableTableProperty")
public final class AppendableTableProperty<S> {

	private Inputted<S> dialog;

	private TableView<S> table;

	private void append() {
		showAddItemDialog();
		tryPuttingAddedItemToTable();
		table.scrollTo(table.getItems().size() - 1);
	}

	private MenuItem createAppendMenuItem() {
		MenuItem item = new MenuItem("Append");
		item.setOnAction(event -> append());
		return item;
	}

	private ContextMenu createTableMenu() {
		ContextMenu menu = new ContextMenu();
		menu.getItems().addAll(createAppendMenuItem());
		return menu;
	}

	private Stage getStage() {
		Scene scene = table.getScene();
		return (Stage) scene.getWindow();
	}

	private void putAddedItemToTable() {
		S entity = getAddedItem();
		if (entity != null)
			table.getItems().add(entity);
	}

	private void setAppendOnPressedEnterKey() {
		table.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER)
				append();
		});
	}

	private void showAddItemDialog() {
		((Startable) dialog).addParent(getStage()).start();
	}

	private void tryPuttingAddedItemToTable() {
		try {
			putAddedItemToTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void addProperties(TableView<S> table, Inputted<S> dialog) {
		this.table = table;
		this.dialog = dialog;
		table.setContextMenu(createTableMenu());
		setAppendOnPressedEnterKey();
	}

	S getAddedItem() {
		return dialog.getAddedItem();
	}
}
