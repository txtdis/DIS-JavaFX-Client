package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.CustomerReceivable;
import ph.txtdis.fx.table.CustomerReceivableTable;
import ph.txtdis.service.CustomerReceivableService;

@Lazy
@Component("customerReceivableApp")
public class CustomerReceivableApp
		extends AbstractTotaledApp<CustomerReceivableTable, CustomerReceivableService, CustomerReceivable>
		implements Launchable
{

	@Override
	public void start() {
		createTotalDisplays(2);
		super.start();
	}

	@Override
	public void tryOpening(String... id) {
		try {
			open(id);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void open(String[] ids) throws Exception {
		service.listInvoicesByCustomerBetweenTwoDayCounts(ids);
		refresh();
	}
}
