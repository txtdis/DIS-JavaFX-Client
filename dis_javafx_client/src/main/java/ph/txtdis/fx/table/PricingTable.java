package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.fx.dialog.PricingDialog;

@Scope("prototype")
@Component("pricingTable")
public class PricingTable extends AppTable<Price> {

	@Autowired
	private AppendContextMenu<Price> append;

	@Autowired
	private Column<Price, PricingType> type;

	@Autowired
	private Column<Price, BigDecimal> price;

	@Autowired
	private Column<Price, LocalDate> startDate;

	@Autowired
	private Column<Price, Channel> channelLimit;

	@Autowired
	private Column<Price, Boolean> approved;

	@Autowired
	private Column<Price, String> decidedBy;

	@Autowired
	private Column<Price, ZonedDateTime> decidedOn;

	@Autowired
	private Column<Price, String> remarks;

	@Autowired
	private PricingDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(type.ofType(ENUM).width(120).build("Type", "type"),
				price.ofType(CURRENCY).build("Price", "priceValue"),
				startDate.ofType(DATE).build("Start\nDate", "startDate"),
				channelLimit.ofType(OTHERS).width(180).build("Limited\nto", "channelLimit"),
				approved.ofType(BOOLEAN).build("OK'd", "approved"),
				decidedBy.ofType(TEXT).width(120).build("Dis/approved\nby", "decidedBy"),
				decidedOn.ofType(TIMESTAMP).build("Dis/approved\non", "decidedOn"),
				remarks.ofType(TEXT).width(320).build("Remarks", "remarks"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
