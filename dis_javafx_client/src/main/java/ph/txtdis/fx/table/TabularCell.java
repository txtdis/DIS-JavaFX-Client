package ph.txtdis.fx.table;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ph.txtdis.app.Launchable;
import ph.txtdis.app.MultiTyped;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Typed;
import ph.txtdis.fx.dialog.FieldDialog;
import ph.txtdis.type.ModuleType;
import ph.txtdis.type.Type;

@Scope("prototype")
@Component("tabularCell")
public class TabularCell<S extends Keyed<?>, T> {

	private boolean doubleClickable;

	private AppTable<S> table;

	private Launchable app;

	private String[] selectionIds;

	private S tableItem;

	private Stage stage;

	private TableCell<S, T> tableCell;

	private TableColumn<S, T> tableColumn;

	public TableCell<S, T> get(Launchable app, Type type) {
		this.app = app;
		this.doubleClickable = true;
		return textFieldCell(type);
	}

	@SuppressWarnings("unchecked")
	public TableCell<S, T> get(String field) {
		doubleClickable = false;
		return (TableCell<S, T>) new CheckboxCell<S>(field);
	}

	private String getAppType() {
		return (String) tableColumn.getUserData();
	}

	private String getColumnIndex() {
		List<TableColumn<S, ?>> columns = table.getColumns();
		return String.valueOf(columns.indexOf(tableColumn));
	}

	@SuppressWarnings("unchecked")
	private boolean itemExists() {
		TableRow<S> row = tableCell.getTableRow();
		tableItem = row.getItem();
		return tableItem != null;
	}

	private String itemId() {
		return tableItem.getId().toString();
	}

	private void launch() {
		app.addParent(stage);
		if (app instanceof FieldDialog<?>)
			launchDialog();
		else
			launchApp();
	}

	private void launchApp() {
		if (app instanceof MultiTyped)
			((MultiTyped) app).type(type());
		app.start();
		app.actOn(selectionIds);
	}

	private void launchAppIfAble() {
		if (app != null)
			launch();
		else
			setSelectedItem();
	}

	private void launchDialog() {
		app.actOn(selectionIds);
		app.start();
		refreshTable();
	}

	@SuppressWarnings("unchecked")
	private void onMouseClick(MouseEvent e) {
		if (e.getClickCount() < 2)
			return;
		tableCell = (TableCell<S, T>) e.getSource();
		if (itemExists())
			setOnDoubleMouseClicks();
	}

	@SuppressWarnings("unchecked")
	private void refreshTable() {
		ObservableList<S> l = observableArrayList(table.getItems());
		l.set(Integer.valueOf(selectionIds[0]) - 1, ((FieldDialog<S>) app).getAddedItem());
		table.setItems(l);
	}

	private void setOnDoubleMouseClicks() {
		tableColumn = tableCell.getTableColumn();
		table = (AppTable<S>) tableColumn.getTableView();
		selectionIds = new String[] { itemId(), getColumnIndex(), getAppType() };
		stage = (Stage) tableCell.getScene().getWindow();
		launchAppIfAble();
	}

	private void setSelectedItem() {
		table.setItem(tableItem);
		stage.close();
	}

	private TableCell<S, T> textFieldCell(Type type) {
		FieldCell<S, T> cell = new FieldCell<>(type);
		if (doubleClickable)
			cell.setOnMouseClick(e -> onMouseClick(e));
		return cell;
	}

	private ModuleType type() {
		String s = ((Typed) tableItem).type();
		return ModuleType.valueOf(s);
	}
}
