package ph.txtdis.util;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import ph.txtdis.app.Startable;
import ph.txtdis.dto.Account;
import ph.txtdis.dto.AgingReceivableReport;
import ph.txtdis.dto.Authority;
import ph.txtdis.dto.Backup;
import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerReceivableReport;
import ph.txtdis.dto.Delivery;
import ph.txtdis.dto.Invoice;
import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.dto.LoadSettlement;
import ph.txtdis.dto.LoadSettlementAdjustment;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.Picking;
import ph.txtdis.dto.Purchase;
import ph.txtdis.dto.Receiving;
import ph.txtdis.dto.Remittance;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.StockTake;
import ph.txtdis.dto.Style;
import ph.txtdis.dto.Truck;
import ph.txtdis.dto.User;
import ph.txtdis.dto.Vat;
import ph.txtdis.dto.Warehouse;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.util.TypeMap.Type;

@Component
public class TypeMap extends LinkedHashMap<String, Type> {

	protected static class Type {

		private String icon;

		private ParameterizedTypeReference<?> type;

		public Type(String icon, ParameterizedTypeReference<?> type) {
			this.icon = icon;
			this.type = type;
		}

		public String getIcon() {
			return icon;
		}

		public ParameterizedTypeReference<?> getType() {
			return type;
		}
	}

	private static final long serialVersionUID = -1782679034968493608L;

	public TypeMap() {
		put("accounts", new Type("", new ParameterizedTypeReference<List<Account>>() {
		}));
		put("account", new Type("\ue834", new ParameterizedTypeReference<Account>() {
		}));
		put("agingReceivable", new Type("\ue802", new ParameterizedTypeReference<AgingReceivableReport>() {
		}));
		put("back", new Type("\ue803", null));
		put("backup", new Type("\ue821", new ParameterizedTypeReference<Backup>() {
		}));
		put("booking", new Type("\ue829", new ParameterizedTypeReference<Booking>() {
		}));
		put("channels", new Type("", new ParameterizedTypeReference<List<Channel>>() {
		}));
		put("channel", new Type("\ue808", new ParameterizedTypeReference<Channel>() {
		}));
		put("customers", new Type("", new ParameterizedTypeReference<List<Customer>>() {
		}));
		put("customer", new Type("\ue809", new ParameterizedTypeReference<Customer>() {
		}));
		put("customerList", new Type("\ue809", null));
		put("customerReceivable", new Type("\ue802", new ParameterizedTypeReference<CustomerReceivableReport>() {
		}));
		put("dateRange", new Type("\ue807", null));
		// TODO
		put("dayEndReport", new Type("\ue807", new ParameterizedTypeReference<Invoice>() {
		}));
		// TODO
		put("deactivate", new Type("\ue80a", null));
		put("delivery", new Type("\ue805", new ParameterizedTypeReference<Delivery>() {
		}));
		put("excel", new Type("\ue810", null));
		// TODO
		put("inventory", new Type("\ue815", new ParameterizedTypeReference<Invoice>() {
		}));
		put("invoice", new Type("\ue817", new ParameterizedTypeReference<Invoice>() {
		}));
		put("invoiceBooklets", new Type("", new ParameterizedTypeReference<List<InvoiceBooklet>>() {
		}));
		put("invoiceBooklet", new Type("\ue816", new ParameterizedTypeReference<InvoiceBooklet>() {
		}));
		put("items", new Type("", new ParameterizedTypeReference<List<Item>>() {
		}));
		put("item", new Type("\ue819", new ParameterizedTypeReference<Item>() {
		}));
		put("itemFamilies", new Type("", new ParameterizedTypeReference<List<ItemFamily>>() {
		}));
		put("itemFamily", new Type("\ue818", new ParameterizedTypeReference<ItemFamily>() {
		}));
		put("itemTrees", new Type("", new ParameterizedTypeReference<List<ItemTree>>() {
		}));
		put("itemTree", new Type("\ue836", new ParameterizedTypeReference<ItemTree>() {
		}));
		put("loadSettlement", new Type("", new ParameterizedTypeReference<LoadSettlement>() {
		}));
		put("loadSettlementAdjustment", new Type("", new ParameterizedTypeReference<LoadSettlementAdjustment>() {
		}));
		put("locations", new Type("", new ParameterizedTypeReference<List<Location>>() {
		}));
		put("mail", new Type("\ue842", null));
		put("next", new Type("\ue81a", null));
		put("new", new Type("\ue800", null));
		put("openByDate", new Type("\ue807", null));
		put("openByNo", new Type("\ue81b", null));
		put("picking", new Type("\ue83b", new ParameterizedTypeReference<Picking>() {
		}));
		put("purchase", new Type("\ue81d", new ParameterizedTypeReference<Purchase>() {
		}));
		put("receiving", new Type("\ue81e", new ParameterizedTypeReference<Receiving>() {
		}));
		put("remittance", new Type("\ue81f", new ParameterizedTypeReference<Remittance>() {
		}));
		// TODO
		put("return", new Type("\ue83c", new ParameterizedTypeReference<List<Authority>>() {
		}));
		put("roles", new Type("", new ParameterizedTypeReference<List<Authority>>() {
		}));
		put("role", new Type("\ue82c", new ParameterizedTypeReference<Authority>() {
		}));
		put("routes", new Type("", new ParameterizedTypeReference<List<Route>>() {
		}));
		put("route", new Type("\ue822", new ParameterizedTypeReference<Route>() {
		}));
		// TODO
		put("salesReport", new Type("\ue820", new ParameterizedTypeReference<List<Authority>>() {
		}));
		put("save", new Type("\ue823", null));
		put("search", new Type("\ue824", null));
		put("stockTake", new Type("\ue828", new ParameterizedTypeReference<StockTake>() {
		}));
		// TODO
		put("stockTakeReconciliation", new Type("\ue82a", new ParameterizedTypeReference<StockTake>() {
		}));
		put("styles", new Type("", new ParameterizedTypeReference<List<Style>>() {
		}));
		put("style", new Type("\ue825", new ParameterizedTypeReference<Style>() {
		}));
		put("trucks", new Type("", new ParameterizedTypeReference<List<Truck>>() {
		}));
		put("truck", new Type("\ue838", new ParameterizedTypeReference<Truck>() {
		}));
		put("users", new Type("", new ParameterizedTypeReference<List<User>>() {
		}));
		put("user", new Type("\ue82d", new ParameterizedTypeReference<User>() {
		}));
		put("vats", new Type("", new ParameterizedTypeReference<List<Vat>>() {
		}));
		put("vat", new Type("\ue82f", new ParameterizedTypeReference<Vat>() {
		}));
		put("warehouses", new Type("", new ParameterizedTypeReference<List<Warehouse>>() {
		}));
		put("warehouse", new Type("\ue830", new ParameterizedTypeReference<Warehouse>() {
		}));
	}

	public String icon(AppButton b) {
		return icon(Text.toName(b));
	}

	public String icon(Startable a) {
		return icon(Text.toName(a));
	}

	public String icon(String path) {
		Type type = get(path);
		return type == null ? null : type.getIcon();
	}

	public ParameterizedTypeReference<?> type(String path) {
		Type type = get(path);
		return type == null ? null : type.getType();
	}
}
