package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.OTHERS;
import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.Route;
import ph.txtdis.type.Type;

@Component("customerListTable")
public class CustomerListTable extends AppTable<Customer> {

	@Autowired
	private Column<Customer, Long> id;

	@Autowired
	private Column<Customer, String> name;

	@Autowired
	private Column<Customer, Route> route;

	@Autowired
	private Column<Customer, String> street;

	@Autowired
	private Column<Customer, Location> barangay;

	@Autowired
	private Column<Customer, Location> city;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
	// @formatter:off
		getColumns().setAll(
			id.ofType(ID).build("ID No.", "id"),
			name.ofType(Type.TEXT).build("Name", "name"),
			route.ofType(OTHERS).build("Route", "route"),
			street.ofType(TEXT).build("Street", "street"),
			barangay.ofType(OTHERS).width(180).build("Barangay", "barangay"),
			city.ofType(OTHERS).build("City", "city"));
	// @formatter:on
	}
}
