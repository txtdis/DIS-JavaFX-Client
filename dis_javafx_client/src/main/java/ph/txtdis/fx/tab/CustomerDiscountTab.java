package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.table.CustomerDiscountTable;
import ph.txtdis.service.CustomerService;

@Scope("prototype")
@Component("customerDiscountTab")
public class CustomerDiscountTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private CustomerDiscountTable customerDiscountTable;

	public CustomerDiscountTab() {
		super("Customer Discount");
	}

	@Override
	public void refresh() {
		customerDiscountTable.items(service.get().getCustomerDiscounts());
	}

	@Override
	public void save() {
		service.get().setCustomerDiscounts(customerDiscountTable.getItems());
	}

	@Override
	protected VBox mainVerticalPane() {
		VBox vbox = super.mainVerticalPane();
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(box.forHorizontalPane(customerDiscountTable.build()));
	}
}
