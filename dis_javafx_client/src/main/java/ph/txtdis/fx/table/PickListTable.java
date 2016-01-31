package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.SalesApp;
import ph.txtdis.dto.Booking;

@Lazy
@Component("pickListTable")
public class PickListTable extends AppTable<Booking> {

	@Autowired
	private SalesApp app;

	@Autowired
	private PickListTableContextMenu menu;

	@Autowired
	private Column<Booking, Long> id;

	@Autowired
	private Column<Booking, String> name, barangay, route;

	public void addMenu() {
		menu.setMenu(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.launches(app).ofType(ID).build("S/O No.", "id"), //
				name.launches(app).ofType(TEXT).build("Name", "customer"), //
				barangay.launches(app).ofType(TEXT).width(360).build("Location", "location"), //
				route.launches(app).ofType(TEXT).width(180).build("Route", "route"));
	}
}
