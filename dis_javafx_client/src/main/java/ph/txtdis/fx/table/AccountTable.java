package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Account;
import ph.txtdis.fx.dialog.AccountDialog;
import ph.txtdis.type.Type;

@Lazy
@Component("accountTable")
public class AccountTable extends AppTable<Account> {

	@Autowired
	private AppendableTableProperty<Account> append;

	@Autowired
	private Column<Account, String> assignedSeller;

	@Autowired
	private Column<Account, LocalDate> startDate;

	@Autowired
	private Column<Account, String> assignedBy;

	@Autowired
	private Column<Account, ZonedDateTime> assignedOn;

	@Autowired
	private AccountDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(assignedSeller.ofType(TEXT).width(100).build("Assigned\nSeller", "seller"),
				startDate.ofType(Type.DATE).build("Start\nDate", "startDate"),
				assignedBy.ofType(TEXT).width(100).build("Assigned\nby", "createdBy"),
				assignedOn.ofType(TIMESTAMP).build("Assigned\non", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
	}
}
