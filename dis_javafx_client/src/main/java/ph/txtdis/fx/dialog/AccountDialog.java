package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Account;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.service.RouteService;

@Component("accountDialog")
public class AccountDialog extends FieldDialog<Account> {

	@Autowired
	private LabeledCombo<String> sellerCombo;

	@Autowired
	private LabeledDatePicker startDatePicker;

	@Autowired
	private RouteService service;

	private List<String> getSellers() {
		try {
			return service.listUsers();
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}

	private Account save(Account ib) {
		try {
			return service.save(sellerCombo.getValue(), startDatePicker.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			resetNodesOnError(e);
			return null;
		}
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		sellerCombo.name("Seller").items(getSellers()).build();
		startDatePicker.name("Start Date");
		return Arrays.asList(sellerCombo, startDatePicker);
	}

	@Override
	protected Account createEntity() {
		return save(new Account());
	}
}
