package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Account;
import ph.txtdis.fx.table.AccountTable;
import ph.txtdis.service.RouteService;

@Lazy
@Component("accountApp")
public class AccountApp extends AbstractTableApp<AccountTable, RouteService, Account> implements Launchable {

	@Override
	public void refresh() {
		try {
			table.items(service.getSellerHistory());
			updateTitleAndHeader();
			setFocus();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void tryOpening(String... ids) {
		try {
			open(ids);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void open(String[] ids) throws Exception {
		service.find(ids);
		refresh();
	}

	@Override
	protected String headerText() {
		return service.getRoute() + " Seller List";
	}

	@Override
	protected String titleText() {
		return service.getRoute() + " Seller History";
	}
}
