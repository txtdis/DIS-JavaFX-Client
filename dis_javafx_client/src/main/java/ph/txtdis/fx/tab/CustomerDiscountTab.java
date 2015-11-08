package ph.txtdis.fx.tab;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.table.CustomerDiscountTable;
import ph.txtdis.service.CustomerService;

@Lazy
@Component("customerDiscountTab")
public class CustomerDiscountTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private CustomerDiscountTable table;

	public CustomerDiscountTab() {
		super("Customer Discount");
	}

	@Override
	public void refresh() {
		table.items(service.getDiscounts());
	}

	@Override
	public void save() {
		service.setDiscounts(table.getItems());
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return Arrays.asList(box.forHorizontalPane(table.build()));
	}
}
