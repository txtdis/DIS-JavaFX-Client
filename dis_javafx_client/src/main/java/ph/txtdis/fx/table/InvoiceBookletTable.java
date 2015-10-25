package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ALPHA;
import static ph.txtdis.type.Type.CODE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.User;
import ph.txtdis.fx.dialog.InvoiceBookletDialog;

@Lazy
@Component("invoiceBookletTable")
public class InvoiceBookletTable extends AppTable<InvoiceBooklet> {

	@Autowired
	private AppendContextMenu<InvoiceBooklet> append;

	@Autowired
	private Column<InvoiceBooklet, String> prefix;

	@Autowired
	private Column<InvoiceBooklet, String> suffix;

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
		getColumns().setAll(prefix.ofType(CODE).width(60).build("Code", "prefix"),
				startId.ofType(ID).build("First No.", "startId"), endId.ofType(ID).build("Last No.", "endId"),
				suffix.ofType(ALPHA).width(65).build("Series", "suffix"),
				issuedTo.ofType(OTHERS).width(100).build("Issued to", "issuedTo"),
				issuedBy.ofType(TEXT).width(120).build("Issued by", "createdBy"),
				issuedOn.ofType(TIMESTAMP).build("Issued on", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
