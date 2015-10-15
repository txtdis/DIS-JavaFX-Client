package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.app.AccountApp;
import ph.txtdis.dto.Route;
import ph.txtdis.fx.dialog.RouteDialog;

@Lazy
@Component("routeTable")
public class RouteTable extends NameListTable<Route, RouteDialog> {

	@Autowired
	private AccountApp accountApp;

	@Autowired
	protected Column<Route, String> seller;

	@Autowired
	protected Column<Route, String> assignedBy;

	@Autowired
	protected Column<Route, ZonedDateTime> assignedOn;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(id.ofType(ID).launches(accountApp).build("ID No.", "id"),
				name.ofType(TEXT).launches(accountApp).width(180).build("Name", "name"),
				createdBy.ofType(TEXT).launches(accountApp).width(100).build("Created by", "createdBy"),
				createdOn.ofType(TIMESTAMP).launches(accountApp).build("Created on", "createdOn"),
				seller.ofType(TEXT).launches(accountApp).width(100).build("Current Seller", "seller"),
				assignedBy.ofType(TEXT).launches(accountApp).width(100).build("Assigned by", "assignedBy"),
				assignedOn.ofType(TIMESTAMP).launches(accountApp).build("Assigned\non", "assignedOn"));
	}

}
