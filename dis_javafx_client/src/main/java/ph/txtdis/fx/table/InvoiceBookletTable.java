package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.User;
import ph.txtdis.fx.dialog.InvoiceBookletDialog;

@Component("invoiceBookletTable")
public class InvoiceBookletTable extends AppTable<InvoiceBooklet> {

	@Autowired
	private AppendableTableProperty<InvoiceBooklet> append;

	@Autowired
	private Column<InvoiceBooklet, Long> startId;

	@Autowired
	private Column<InvoiceBooklet, Long> endId;

	@Autowired
	private Column<InvoiceBooklet, User> issuedTo;

	@Autowired
	private Column<InvoiceBooklet, String> issuedBy;

	@Autowired
	private Column<InvoiceBooklet, ZonedDateTime> issuedOn;

	@Autowired
	private InvoiceBookletDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(startId.ofType(ID).build("First No.", "startId"),
				endId.ofType(ID).build("Last No.", "endId"),
				issuedTo.ofType(OTHERS).width(100).build("Issued to", "issuedTo"),
				issuedBy.ofType(TEXT).width(120).build("Issued by", "createdBy"),
				issuedOn.ofType(TIMESTAMP).build("Issued on", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
	}
}
