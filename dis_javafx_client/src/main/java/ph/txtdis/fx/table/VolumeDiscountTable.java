package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.INTEGER;
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
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.fx.dialog.VolumeDiscountDialog;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;

@Scope("prototype")
@Component("volumeDiscountTable")
public class VolumeDiscountTable extends AppTable<VolumeDiscount> {

	@Autowired
	private AppendContextMenu<VolumeDiscount> append;

	@Autowired
	private Column<VolumeDiscount, VolumeDiscountType> type;

	@Autowired
	private Column<VolumeDiscount, UomType> uom;

	@Autowired
	private Column<VolumeDiscount, Integer> cutoff;

	@Autowired
	private Column<VolumeDiscount, BigDecimal> discount;

	@Autowired
	private Column<VolumeDiscount, LocalDate> startDate;

	@Autowired
	private Column<VolumeDiscount, Channel> channelLimit;

	@Autowired
	private Column<VolumeDiscount, Boolean> approved;

	@Autowired
	private Column<VolumeDiscount, String> decidedBy;

	@Autowired
	private Column<VolumeDiscount, ZonedDateTime> decidedOn;

	@Autowired
	private Column<VolumeDiscount, String> remarks;

	@Autowired
	private VolumeDiscountDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(type.ofType(ENUM).width(120).build("Type", "type"), //
				uom.ofType(ENUM).width(80).build("UOM", "uom"),
				cutoff.ofType(INTEGER).width(80).build("Target\nVolume", "cutoff"),
				discount.ofType(CURRENCY).build("Discount", "discount"),
				startDate.ofType(DATE).build("Start\nDate", "startDate"),
				channelLimit.ofType(OTHERS).width(180).build("Limited\nto", "channelLimit"),
				approved.ofType(BOOLEAN).build("OK'd", "approved"),
				decidedBy.ofType(TEXT).width(120).build("Dis/approved\nby", "decidedBy"),
				decidedOn.ofType(TIMESTAMP).build("Dis/approved\non", "decidedOn"),
				remarks.ofType(TEXT).width(280).build("Remarks", "remarks"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
