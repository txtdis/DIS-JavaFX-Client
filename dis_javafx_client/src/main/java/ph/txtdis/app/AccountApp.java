package ph.txtdis.app;

import org.apache.commons.lang3.StringUtils;
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
	public void launch(String... ids) {
		try {
			open(ids);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private String capitalizedModule() {
		return StringUtils.capitalize(service.getModule());
	}

	private void open(String[] ids) throws Exception {
		service.find(ids);
		refresh();
	}

	@Override
	protected String getHeaderText() {
		return capitalizedModule() + " Seller List";
	}

	@Override
	protected String getTitleText() {
		return capitalizedModule() + " Seller History";
	}
}
