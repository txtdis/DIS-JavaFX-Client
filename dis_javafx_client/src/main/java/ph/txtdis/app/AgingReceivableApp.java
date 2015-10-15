package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.AgingReceivable;
import ph.txtdis.fx.table.AgingReceivableTable;
import ph.txtdis.service.AgingReceivableService;

@Lazy
@Component("agingReceivableApp")
public class AgingReceivableApp
		extends AbstractTotaledApp<AgingReceivableTable, AgingReceivableService, AgingReceivable>
{

	@Override
	public void start() {
		createTotalDisplays(7);
		super.start();
	}
}
