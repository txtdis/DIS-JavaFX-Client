package ph.txtdis.fx.table;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ph.txtdis.app.Launchable;
import ph.txtdis.app.MultiTyped;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Typed;
import ph.txtdis.type.ModuleType;
import ph.txtdis.type.Type;

@Scope("prototype")
@Component("tabularCell")
public class TabularCell<S extends Keyed<?>, T> {

	private AppTable<S> table;

	private Launchable app;

	private String[] selectionIds;

	private S tableItem;

	private Stage stage;

	private TableCell<S, T> tableCell;

	private TableColumn<S, T> tableColumn;

	public TableCell<S, T> get(Launchable app, Type type) {
		FieldCell<S, T> cell = new FieldCell<>(type);
		cell.setOnMouseClick(e -> onMouseClick(e, app));
		return cell;
	}

	private String getAppType() {
		return (String) tableColumn.getUserData();
	}

	private String getColumnIndex() {
		List<TableColumn<S, ?>> columns = table.getColumns();
		return String.valueOf(columns.indexOf(tableColumn));
	}

	@SuppressWarnings("unchecked")
	private String getItemId() {
		TableRow<S> row = tableCell.getTableRow();
		tableItem = row.getItem();
		return tableItem == null ? null : tableItem.getId().toString();
	}

	private void launchApp() {
		startApp();
		app.launch(selectionIds);
	}

	private void launchAppIfAble() {
		if (app != null)
			launchApp();
		else
			setSelectedItem();
	}

	private void onDoubleMouseClicks() {
		setTableColumn();
		setAppTable();
		setSelectionIds();
		setStage();
		launchAppIfAble();
	}

	@SuppressWarnings("unchecked")
	private void onMouseClick(MouseEvent e, Launchable app) {
		if (e.getClickCount() > 1) {
			this.app = app;
			tableCell = (TableCell<S, T>) e.getSource();
			onDoubleMouseClicks();
		}
	}

	private void setAppTable() {
		table = (AppTable<S>) tableColumn.getTableView();
	}

	private void setSelectedItem() {
		table.setItem(tableItem);
		stage.close();
	}

	private void setSelectionIds() {
		selectionIds = new String[] { getItemId(), getColumnIndex(), getAppType() };
	}

	private void setStage() {
		Scene scene = tableCell.getScene();
		stage = (Stage) scene.getWindow();
	}

	private void setTableColumn() {
		tableColumn = tableCell.getTableColumn();
	}

	private void startApp() {
		app.addParent(stage);
		if (app instanceof MultiTyped)
			((MultiTyped) app).type(type());
		app.start();
	}

	private ModuleType type() {
		String s = ((Typed) tableItem).type();
		return ModuleType.valueOf(s);
	}
}
