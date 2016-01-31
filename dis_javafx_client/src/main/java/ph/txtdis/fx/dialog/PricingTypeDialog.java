package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.PricingType;
import ph.txtdis.service.PricingTypeService;

@Scope("prototype")
@Component("pricingTypeDialog")
public class PricingTypeDialog extends NameListDialog<PricingType, PricingTypeService> {

	@Override
	protected String headerText() {
		return "Add New Pricing Type";
	}
}
