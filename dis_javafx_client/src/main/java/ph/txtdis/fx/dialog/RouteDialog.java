package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Route;
import ph.txtdis.service.RouteService;

@Lazy
@Component("routeDialog")
public class RouteDialog extends NameListDialog<Route, RouteService> {
}
