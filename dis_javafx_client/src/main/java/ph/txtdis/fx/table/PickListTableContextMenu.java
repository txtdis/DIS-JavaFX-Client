package ph.txtdis.fx.table;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import ph.txtdis.dto.Billable;
import ph.txtdis.dto.Route;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.service.PickListService;

@Lazy
@Component("pickListTableContextMenu")
public final class PickListTableContextMenu {

	@Autowired
	private MessageDialog dialog;

	@Autowired
	private PickListService service;

	private TableView<Billable> table;

	private void addBookingMenuItemsToDeleteMenu(TableView<Billable> t, ContextMenu m) {
		t.getContextMenu().getItems().forEach(b -> {
			MenuItem i = new MenuItem(b.getText());
			i.setGraphic(b.getGraphic());
			i.setOnAction(b.getOnAction());
			m.getItems().add(i);
		});
	}

	private void addBookings(Route r) {
		ObservableList<Billable> c = FXCollections.observableArrayList(table.getItems());
		c.addAll(service.listBookings(r));
		table.setItems(c);
	}

	private void append(Route r) {
		addBookings(r);
		service.get().setBookings(table.getItems());
		refreshTable();
	}

	private MenuItem deleteMenuItem(TableRow<Billable> r) {
		MenuItem i = new MenuItem("Delete row");
		i.setOnAction(e -> deleteRow(r));
		return i;
	}

	private void deleteRow(TableRow<Billable> r) {
		table.getItems().remove(r.getItem());
		service.unpick(r.getItem());
		service.get().setBookings(table.getItems());
		refreshTable();
	}

	private ContextMenu menu() throws Exception {
		ContextMenu m = new ContextMenu();
		m.getItems().setAll(menuItems());
		return m;
	}

	private MenuItem menuItem(Route r) {
		MenuItem i = new MenuItem(r.getName());
		i.setOnAction(e -> append(r));
		return i;
	}

	private List<MenuItem> menuItems() throws Exception {
		List<MenuItem> i = new ArrayList<>();
		service.listRoutes().forEach(r -> i.add(menuItem(r)));
		return i;
	}

	private void refreshTable() {
		setMenu(table);
		table.refresh();
		table.scrollTo(table.getItems().size() - 1);
	}

	private TableRow<Billable> row(TableView<Billable> t) {
		// @formatter:off
        TableRow<Billable> r = new TableRow<>();
        r.contextMenuProperty().bind(Bindings
                .when(r.itemProperty().isNotNull())
                .then(rowMenu(t, r))
                .otherwise((ContextMenu) null));
        return r;
        // @formatter:on
	}

	private ContextMenu rowMenu(TableView<Billable> t, TableRow<Billable> r) {
		ContextMenu m = new ContextMenu();
		addBookingMenuItemsToDeleteMenu(t, m);
		m.getItems().add(deleteMenuItem(r));
		return m;
	}

	void setMenu(TableView<Billable> table) {
		try {
			this.table = table;
			table.setContextMenu(menu());
			table.setRowFactory(t -> row(t));
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(table).start();
		}
	}
}
