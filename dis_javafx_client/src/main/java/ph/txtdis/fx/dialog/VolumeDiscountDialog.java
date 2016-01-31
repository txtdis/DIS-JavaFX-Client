package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.CURRENCY;
import static ph.txtdis.type.Type.INTEGER;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.ItemService;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;

@Scope("prototype")
@Component("volumeDiscountDialog")
public class VolumeDiscountDialog extends FieldDialog<VolumeDiscount> {

	@Autowired
	private ItemService service;

	@Autowired
	private LabeledCombo<Channel> channelLimitCombo;

	@Autowired
	private LabeledCombo<UomType> uomCombo;

	@Autowired
	private LabeledCombo<VolumeDiscountType> typeCombo;

	@Autowired
	private LabeledField<BigDecimal> discountField;

	@Autowired
	private LabeledField<Integer> cutoffField;

	@Autowired
	private LabeledDatePicker startDatePicker;

	private VolumeDiscount discount;

	private void createDiscountUponValidation() {
		if (startDatePicker.getValue() != null)
			try {
				discount = service.createDiscountUponValidation(//
						typeCombo.getValue(), //
						uomCombo.getValue(), //
						cutoffField.getValue(), //
						discountField.getValue(), //
						channelLimitCombo.getValue(), //
						startDatePicker.getValue());
			} catch (Exception e) {
				resetNodesOnError(e);
			}
	}

	private List<Channel> channels() {
		try {
			return service.listAllChannels();
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	private LabeledDatePicker startDatePicker() {
		startDatePicker.name("Start Date");
		startDatePicker.setOnAction(value -> createDiscountUponValidation());
		return startDatePicker;
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		typeCombo.name("Type").items(VolumeDiscountType.values()).build();
		uomCombo.name("UOM").items(service.listSellingUoms()).build();
		cutoffField.name("Level").build(INTEGER);
		discountField.name("Discount").build(CURRENCY);
		channelLimitCombo.name("Only for").items(channels()).build();
		return asList(typeCombo, uomCombo, cutoffField, discountField, channelLimitCombo, startDatePicker());
	}

	@Override
	protected VolumeDiscount createEntity() {
		return discount;
	}

	@Override
	protected String headerText() {
		return "Add New Discount";
	}
}