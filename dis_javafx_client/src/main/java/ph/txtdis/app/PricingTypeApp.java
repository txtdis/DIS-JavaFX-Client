package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.PricingType;
import ph.txtdis.fx.table.PricingTypeTable;
import ph.txtdis.service.PricingTypeService;

@Lazy
@Component("pricingTypeApp")
public class PricingTypeApp extends AbstractTableApp<PricingTypeTable, PricingTypeService, PricingType> {

	@Override
	protected String getHeaderText() {
		return service.getHeaderText();
	}

	@Override
	protected String getTitleText() {
		return service.getTitleText();
	}
}
