package ph.txtdis.app;

import org.springframework.stereotype.Component;

import ph.txtdis.dto.Customer;
import ph.txtdis.fx.table.CustomerListTable;
import ph.txtdis.service.CustomerService;

@Component("customerListApp")
public class CustomerListApp extends AbstractExcelApp<CustomerListTable, CustomerService, Customer> {

	public Customer getSelection() {
		return table.getItem();
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		refresh();
		showAndWait();
	}

	@Override
	protected String headerText() {
		return "Customer List";
	}

	@Override
	protected String titleText() {
		return headerText();
	}
}
