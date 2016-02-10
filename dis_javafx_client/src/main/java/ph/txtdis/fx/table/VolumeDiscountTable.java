package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.ENUM;
import static ph.txtdis.type.Type.INTEGER;
import static ph.txtdis.type.Type.OTHERS;

import java.math.BigDecimal;

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
	private Column<VolumeDiscount, VolumeDiscountType> type;

	@Autowired
	private Column<VolumeDiscount, UomType> uom;

	@Autowired
	private Column<VolumeDiscount, Integer> cutoff;

	@Autowired
	private Column<VolumeDiscount, BigDecimal> discount;

	@Autowired
	private Column<VolumeDiscount, Channel> channelLimit;

	@Autowired
	private DecisionNeededTableControls<VolumeDiscount> decisionNeeded;

	@Autowired
	private VolumeDiscountDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(type.ofType(ENUM).width(120).build("Type", "type"), //
				uom.ofType(ENUM).width(80).build("UOM", "uom"),
				cutoff.ofType(INTEGER).width(80).build("Target\nVolume", "cutoff"),
				discount.ofType(CURRENCY).build("Discount", "discount"),
				channelLimit.ofType(OTHERS).width(180).build("Limited\nto", "channelLimit"));
		getColumns().addAll(decisionNeeded.addColumns());
	}

	@Override
	protected void addProperties() {
		decisionNeeded.addContextMenu(this, dialog);
	}
}
