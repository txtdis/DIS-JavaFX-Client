package ph.txtdis.util;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import ph.txtdis.app.Startable;
import ph.txtdis.dto.Account;
import ph.txtdis.dto.AgingReceivableReport;
import ph.txtdis.dto.Authority;
import ph.txtdis.dto.Billable;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditNote;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerReceivableReport;
import ph.txtdis.dto.Holiday;
import ph.txtdis.dto.Inventory;
import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.dto.Location;
import ph.txtdis.dto.Payment;
import ph.txtdis.dto.PickList;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.SalesRevenue;
import ph.txtdis.dto.SalesVolume;
import ph.txtdis.dto.Script;
import ph.txtdis.dto.StockTake;
import ph.txtdis.dto.Style;
import ph.txtdis.dto.Truck;
import ph.txtdis.dto.User;
import ph.txtdis.dto.Vat;
import ph.txtdis.dto.Warehouse;
import ph.txtdis.util.TypeMap.Type;

@Component
public class TypeMap extends LinkedHashMap<String, Type> {

	protected static final class Type {

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
		put("accept", new Type("\ue845", null));
		put("accounts", new Type(null, new ParameterizedTypeReference<List<Account>>() {
		}));
		put("account", new Type("\ue834", new ParameterizedTypeReference<Account>() {
		}));
		put("agingReceivable", new Type("\ue802", new ParameterizedTypeReference<AgingReceivableReport>() {
		}));
		put("back", new Type("\ue803", null));
		put("badOrder", new Type("\ue80a", new ParameterizedTypeReference<Billable>() {
		}));
		put("billable", new Type(null, new ParameterizedTypeReference<Billable>() {
		}));
		put("billables", new Type(null, new ParameterizedTypeReference<List<Billable>>() {
		}));
		put("channel", new Type("\ue808", new ParameterizedTypeReference<Channel>() {
		}));
		put("channels", new Type(null, new ParameterizedTypeReference<List<Channel>>() {
		}));
		put("checkSearch", new Type("\ue904", null));
		put("cheque", new Type("\ue90f", null));
		put("creditNote", new Type("\ue806", new ParameterizedTypeReference<CreditNote>() {
		}));
		put("creditNotes", new Type(null, new ParameterizedTypeReference<List<CreditNote>>() {
		}));
		put("customer", new Type("\ue809", new ParameterizedTypeReference<Customer>() {
		}));
		put("customers", new Type(null, new ParameterizedTypeReference<List<Customer>>() {
		}));
		put("customerList", new Type("\ue809", null));
		put("customerReceivable", new Type("\ue802", new ParameterizedTypeReference<CustomerReceivableReport>() {
		}));
		put("dataDump", new Type("\ue917", null));
		put("dateRange", new Type("\ue807", null));
		put("deactivate", new Type("\ue903", null));
		put("decision", new Type("\ue900", null));
		put("deliveryReport", new Type("\ue906", new ParameterizedTypeReference<Billable>() {
		}));
		put("deposit", new Type("\ue913", null));
		put("disposal", new Type("\ue90d", null));
		put("download", new Type("\uf0ed", null));
		put("edit", new Type("\ue80d", null));
		put("excel", new Type("\ue810", null));
		put("holiday", new Type("\ue914", new ParameterizedTypeReference<Holiday>() {
		}));
		put("holidays", new Type(null, new ParameterizedTypeReference<List<Holiday>>() {
		}));
		put("inventory", new Type("\ue814", new ParameterizedTypeReference<Inventory>() {
		}));
		put("inventories", new Type(null, new ParameterizedTypeReference<List<Inventory>>() {
		}));
		put("invoice", new Type("\ue817", null));
		put("invoiceBooklet", new Type("\ue816", new ParameterizedTypeReference<InvoiceBooklet>() {
		}));
		put("invoiceBooklets", new Type(null, new ParameterizedTypeReference<List<InvoiceBooklet>>() {
		}));
		put("item", new Type("\ue819", new ParameterizedTypeReference<Item>() {
		}));
		put("items", new Type(null, new ParameterizedTypeReference<List<Item>>() {
		}));
		put("itemFamily", new Type("\ue836", new ParameterizedTypeReference<ItemFamily>() {
		}));
		put("itemFamilies", new Type(null, new ParameterizedTypeReference<List<ItemFamily>>() {
		}));
		put("itemTree", new Type("\ue852", new ParameterizedTypeReference<ItemTree>() {
		}));
		put("itemTrees", new Type(null, new ParameterizedTypeReference<List<ItemTree>>() {
		}));
		put("list", new Type("\ue906", null));
		put("locations", new Type(null, new ParameterizedTypeReference<List<Location>>() {
		}));
		put("mail", new Type("\ue842", null));
		put("next", new Type("\ue81a", null));
		put("new", new Type("\ue800", null));
		put("openByDate", new Type("\ue807", null));
		put("openByNo", new Type("\ue81b", null));
		put("pickList", new Type("\ue805", new ParameterizedTypeReference<PickList>() {
		}));
		put("pickLists", new Type(null, new ParameterizedTypeReference<List<PickList>>() {
		}));
		put("pricingType", new Type("\ue911", new ParameterizedTypeReference<PricingType>() {
		}));
		put("pricingTypes", new Type(null, new ParameterizedTypeReference<List<PricingType>>() {
		}));
		put("print", new Type("\ue81c", null));
		put("purchaseOrder", new Type("\ue81d", new ParameterizedTypeReference<Billable>() {
		}));
		put("purchaseReceipt", new Type("\ue90a", new ParameterizedTypeReference<Billable>() {
		}));
		put("remittance", new Type("\ue837", new ParameterizedTypeReference<Payment>() {
		}));
		put("remittances", new Type(null, new ParameterizedTypeReference<List<Payment>>() {
		}));
		put("reject", new Type("\ue846", null));
		put("returnOrder", new Type("\ue83c", new ParameterizedTypeReference<Billable>() {
		}));
		put("returnReceipt", new Type("\ue90c", null));
		put("revenueReport", new Type("\ue81f", null));
		put("role", new Type("\ue912", new ParameterizedTypeReference<Authority>() {
		}));
		put("roles", new Type(null, new ParameterizedTypeReference<List<Authority>>() {
		}));
		put("route", new Type("\ue822", new ParameterizedTypeReference<Route>() {
		}));
		put("routes", new Type(null, new ParameterizedTypeReference<List<Route>>() {
		}));
		put("salesOrder", new Type("\ue829", new ParameterizedTypeReference<Billable>() {
		}));
		put("salesOrders", new Type(null, new ParameterizedTypeReference<List<Billable>>() {
		}));
		put("salesReturn", new Type("\ue81e", new ParameterizedTypeReference<Billable>() {
		}));
		put("salesRevenue", new Type("\ue820", null));
		put("salesRevenues", new Type(null, new ParameterizedTypeReference<List<SalesRevenue>>() {
		}));
		put("salesVolume", new Type("\ue80b", null));
		put("salesVolumes", new Type(null, new ParameterizedTypeReference<List<SalesVolume>>() {
		}));
		put("save", new Type("\ue823", null));
		put("script", new Type(null, new ParameterizedTypeReference<Script>() {
		}));
		put("scripts", new Type(null, new ParameterizedTypeReference<List<Script>>() {
		}));
		put("search", new Type("\ue824", null));
		put("settingsMenu", new Type("\ue801", null));
		put("stockTake", new Type("\ue84e", new ParameterizedTypeReference<StockTake>() {
		}));
		// TODO
		put("stockTakeReconciliation", new Type("\ue907", new ParameterizedTypeReference<StockTake>() {
		}));
		put("style", new Type("\ue825", new ParameterizedTypeReference<Style>() {
		}));
		put("styles", new Type(null, new ParameterizedTypeReference<List<Style>>() {
		}));
		put("sync", new Type(null, new ParameterizedTypeReference<String>() {
		}));
		// TODO
		put("transfer", new Type("\ue833", null));
		put("truck", new Type("\ue838", new ParameterizedTypeReference<Truck>() {
		}));
		put("trucks", new Type(null, new ParameterizedTypeReference<List<Truck>>() {
		}));
		put("upload", new Type("\uf0ee", null));
		put("user", new Type("\ue82d", new ParameterizedTypeReference<User>() {
		}));
		put("users", new Type(null, new ParameterizedTypeReference<List<User>>() {
		}));
		put("vat", new Type("\ue844", new ParameterizedTypeReference<Vat>() {
		}));
		put("vats", new Type(null, new ParameterizedTypeReference<List<Vat>>() {
		}));
		put("warehouse", new Type("\ue830", new ParameterizedTypeReference<Warehouse>() {
		}));
		put("warehouses", new Type(null, new ParameterizedTypeReference<List<Warehouse>>() {
		}));
	}

	public String icon(Startable a) {
		return icon(TextUtils.toName(a));
	}

	public String icon(String name) {
		Type type = get(name);
		return type == null ? null : type.getIcon();
	}

	public ParameterizedTypeReference<?> type(String path) {
		Type type = get(path);
		return type == null ? null : type.getType();
	}
}
