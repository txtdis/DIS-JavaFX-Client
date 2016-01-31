package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.CURRENCY;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Channel;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.control.LabeledCombo;
import ph.txtdis.fx.control.LabeledDatePicker;
import ph.txtdis.fx.control.LabeledField;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("pricingDialog")
public class PricingDialog extends FieldDialog<Price> {

	@Autowired
	private ItemService service;

	@Autowired
	private LabeledCombo<Channel> channelLimitCombo;

	@Autowired
	private LabeledCombo<PricingType> typeCombo;

	@Autowired
	private LabeledField<BigDecimal> priceField;

	@Autowired
	private LabeledDatePicker startDatePicker;

	private Price price;

	private List<Channel> channels() {
		try {
			return service.listAllChannels();
		} catch (Exception e) {
			resetNodesOnError(e);
			return null;
		}
	}

	private void createPricingUponValidation() {
		if (startDatePicker.getValue() != null)
			try {
				price = service.createPricingUponValidation(//
						typeCombo.getValue(), //
						priceField.getValue(), //
						channelLimitCombo.getValue(), //
						startDatePicker.getValue());
			} catch (Exception e) {
				resetNodesOnError(e);
			}
	}

	private LabeledDatePicker startDatePicker() {
		startDatePicker.name("Start Date");
		startDatePicker.setOnAction(value -> createPricingUponValidation());
		return startDatePicker;
	}

	@Override
	protected List<InputNode<?>> addNodes() {
		typeCombo.name("Type").items(service.listPricingTypes()).build();
		priceField.name("Price").build(CURRENCY);
		channelLimitCombo.name("Only for").items(channels()).build();
		return asList(typeCombo, priceField, channelLimitCombo, startDatePicker());
	}

	@Override
	protected Price createEntity() {
		return price;
	}

	@Override
	protected String headerText() {
		return "Add New Pricing";
	}
}