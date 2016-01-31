package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.table.PricingTable;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("pricingTab")
public class PricingTab extends AbstractTab {

	@Autowired
	private ItemService service;

	@Autowired
	private PricingTable table;

	public PricingTab() {
		super("Pricing");
	}

	public BooleanBinding hasNoPrices() {
		return table.isEmpty();
	}

	@Override
	public void refresh() {
		table.items(service.get().getPriceList());
	}

	@Override
	public void save() {
		service.get().setPriceList(table.getItems());
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(box.forHorizontalPane(table.build()));
	}
}
