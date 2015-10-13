package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Routing;
import ph.txtdis.fx.dialog.RoutingDialog;
import ph.txtdis.type.Type;

@Component("routingTable")
public class RoutingTable extends AppTable<Routing> {

	@Autowired
	private AppendableTableProperty<Routing> append;

	@Autowired
	private Column<Routing, String> designatedRoute;

	@Autowired
	private Column<Routing, LocalDate> startDate;

	@Autowired
	private Column<Routing, String> designatedBy;

	@Autowired
	private Column<Routing, ZonedDateTime> designatedOn;

	@Autowired
	private RoutingDialog dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(designatedRoute.ofType(TEXT).width(100).build("Designated\nRoute", "route"),
				startDate.ofType(Type.DATE).build("Start\nDate", "startDate"),
				designatedBy.ofType(TEXT).width(100).build("Designated\nby", "createdBy"),
				designatedOn.ofType(TIMESTAMP).build("Designated\non", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addProperties(this, dialog);
	}
}
