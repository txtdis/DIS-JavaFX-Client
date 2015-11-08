package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Billable;
import ph.txtdis.dto.Route;

@Lazy
@Component("pickListTable")
public class PickListTable extends AppTable<Billable> {

	@Autowired
	private PickListTableContextMenu menu;

	@Autowired
	private Column<Billable, Long> id;

	@Autowired
	private Column<Billable, String> name;

	@Autowired
	private Column<Billable, String> barangay;

	@Autowired
	private Column<Billable, Route> route;

	public void addMenu() {
		menu.setMenu(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
	// @formatter:off
		getColumns().setAll(
			id.ofType(ID).build("S/O No.", "id"),
			name.ofType(TEXT).build("Name", "customerName"),
			barangay.ofType(TEXT).width(360).build("Location", "location"),
			route.ofType(OTHERS).build("Route", "route"));
	// @formatter:on
	}
}
