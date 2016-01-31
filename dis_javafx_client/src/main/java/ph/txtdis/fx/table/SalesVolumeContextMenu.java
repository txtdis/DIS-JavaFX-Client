package ph.txtdis.fx.table;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static ph.txtdis.type.SalesVolumeReportType.values;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import ph.txtdis.service.SalesVolumeService;
import ph.txtdis.type.SalesVolumeReportType;

@Lazy
@Component("salesVolumeContextMenu")
public class SalesVolumeContextMenu {

	@Autowired
	private SalesVolumeService service;

	@Autowired
	private SalesVolumeTable table;

	public void setMenu() {
		table.setContextMenu(menu());
	}

	private ContextMenu menu() {
		ContextMenu m = new ContextMenu();
		m.getItems().setAll(menuItems());
		return m;
	}

	private MenuItem menuItem(SalesVolumeReportType t) {
		MenuItem i = new MenuItem(t.toString());
		i.setOnAction(e -> setTable(t));
		return i;
	}

	private List<MenuItem> menuItems() {
		return asList(values()).stream().map(t -> menuItem(t)).collect(toList());
	}

	private void setTable(SalesVolumeReportType t) {
		table.setTableColumnVisibility(t);
		setTableItems(t);
	}

	private void setTableItems(SalesVolumeReportType t) {
		try {
			table.items(service.listPer(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
