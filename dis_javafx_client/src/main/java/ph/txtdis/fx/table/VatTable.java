package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ALPHA;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.InvoiceApp;
import ph.txtdis.dto.Vat;

@Lazy
@Component("vatTable")
public class VatTable extends AppTable<Vat> {

	@Autowired
	private InvoiceApp app;

	@Autowired
	private Column<Vat, String> id;

	@Autowired
	private Column<Vat, String> prefix;

	@Autowired
	private Column<Vat, Long> idNo;

	@Autowired
	private Column<Vat, String> suffix;

	@Autowired
	private Column<Vat, String> customer;

	@Autowired
	private Column<Vat, LocalDate> orderDate;

	@Autowired
	private Column<Vat, BigDecimal> value;

	@Autowired
	private Column<Vat, BigDecimal> vat;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		id.launches(app).ofType(ALPHA).build("Invoice ID", "");
		id.getColumns().setAll(prefix.launches(app).ofType(ALPHA).build("Prefix", "idPrefix"),
				idNo.launches(app).ofType(ID).build("No.", "idNo"),
				suffix.launches(app).ofType(ALPHA).build("Suffix", "idSuffix"));
		getColumns().setAll(id.launches(app).ofType(ID).build("S/I No.", "id"),
				customer.launches(app).ofType(TEXT).width(180).build("Customer", "customer"),
				orderDate.launches(app).ofType(DATE).build("Date", "orderDate"),
				value.launches(app).ofType(CURRENCY).build("Value", "value"),
				vat.launches(app).ofType(CURRENCY).build("Vat", "vatValue"));
	}
}
