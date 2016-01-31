package ph.txtdis.app;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import ph.txtdis.dto.Inventory;
import ph.txtdis.fx.table.InventoryTable;
import ph.txtdis.service.InventoryService;

@Lazy
@Component("inventoryApp")
public class InventoryApp extends AbstractExcelApp<InventoryTable, InventoryService, Inventory> {

	@Autowired
	private TotaledTableApp totaledTableApp;

	@Override
	public void refresh() {
		try {
			table.items(service.list());
			totaledTableApp.refresh(service);
			super.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void start() {
		totaledTableApp.addTotalDisplayPane(2);
		super.start();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(totaledTableApp.addTablePane(table));
	}
}
