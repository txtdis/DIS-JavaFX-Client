package ph.txtdis.fx.table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.PricingType;
import ph.txtdis.fx.dialog.PricingTypeDialog;

@Scope("prototype")
@Component("pricingTypeTable")
public class PricingTypeTable extends NameListTable<PricingType, PricingTypeDialog> {
}
