package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Route;
import ph.txtdis.fx.table.RouteTable;
import ph.txtdis.service.RouteService;

@Lazy
@Component("routeApp")
public class RouteApp extends AbstractTableApp<RouteTable, RouteService, Route> {
}
