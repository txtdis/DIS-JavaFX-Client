package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Route;

@Component("pickTable")
public class PickTable extends AppTable<Booking> {

	@Autowired
	private ReducableTableProperty<Booking> reduce;

	@Autowired
	private Column<Booking, Long> id;

	@Autowired
	private Column<Booking, String> name;

	@Autowired
	private Column<Booking, String> barangay;

	@Autowired
	private Column<Booking, Route> route;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
	// @formatter:off
		getColumns().setAll(
			id.ofType(ID).build("S/O No.", "id"),
			name.ofType(TEXT).build("Name", "customerName"),
			barangay.ofType(TEXT).build("Barangay", "barangay"),
			route.ofType(OTHERS).build("Route", "route"));
	// @formatter:on
	}

	@Override
	protected void addProperties() {
		reduce.addProperties(this);
	}
}
