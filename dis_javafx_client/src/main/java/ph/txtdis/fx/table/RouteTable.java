package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ENUM;
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
import ph.txtdis.type.DeliveryType;

@Lazy
@Component("routeTable")
public class RouteTable extends NameListTable<Route, RouteDialog> {

	@Autowired
	private AccountApp app;

	@Autowired
	private Column<Route, DeliveryType> type;

	@Autowired
	private Column<Route, String> seller;

	@Autowired
	private Column<Route, String> assignedBy;

	@Autowired
	private Column<Route, ZonedDateTime> assignedOn;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(id.ofType(ID).launches(app).build("ID No.", "id"),
				name.ofType(TEXT).launches(app).width(180).build("Name", "name"),
				type.ofType(ENUM).build("Delivery Type", "type"),
				createdBy.ofType(TEXT).launches(app).width(100).build("Created by", "createdBy"),
				createdOn.ofType(TIMESTAMP).launches(app).build("Created on", "createdOn"),
				seller.ofType(TEXT).launches(app).width(100).build("Current Seller", "seller"),
				assignedBy.ofType(TEXT).launches(app).width(100).build("Assigned by", "assignedBy"),
				assignedOn.ofType(TIMESTAMP).launches(app).build("Assigned\non", "assignedOn"));
	}

}
