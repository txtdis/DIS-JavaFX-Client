package ph.txtdis.fx.table;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import ph.txtdis.dto.SellerSold;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.service.SellerFiltered;

@Lazy
@Component("sellerFilterContextMenu")
public class SellerFilterContextMenu<T extends SellerSold> {

	@Autowired
	private MessageDialog dialog;

	private SellerFiltered<T> service;

	private TableView<T> table;

	private ContextMenu menu() throws Exception {
		ContextMenu m = new ContextMenu();
		m.getItems().setAll(menuItems());
		return m;
	}

	private MenuItem menuItem(String seller) {
		MenuItem i = new MenuItem(seller);
		i.setOnAction(e -> setTableItems(seller));
		return i;
	}

	private List<MenuItem> menuItems() {
		return service.listSellers().stream().map(s -> menuItem(s)).collect(toList());
	}

	private void setTableItems(String seller) {
		table.setItems(observableArrayList(service.list(seller)));
	}

	void setMenu(SellerFiltered<T> service, TableView<T> table) {
		try {
			this.service = service;
			this.table = table;
			table.setContextMenu(menu());
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(table).start();
		}
	}
}
