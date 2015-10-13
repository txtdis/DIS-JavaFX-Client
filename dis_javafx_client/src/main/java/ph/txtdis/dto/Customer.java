package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.CustomerType;
import ph.txtdis.type.VisitFrequency;

@Data
@EqualsAndHashCode(callSuper = true)
public class Customer extends AbstractAuditedId<Long> {

	private String name;

	private String deactivatedBy;

	private ZonedDateTime deactivatedOn;

	private String street, contactName, contactSurname, contactTitle, mobile;

	private Location barangay, city, province;

	private CustomerType type;

	private PricingType primaryPricingType, alternatePricingType;

	private Channel channel;

	private VisitFrequency visitFrequency;

	private List<Routing> routeHistory;

	private List<CreditDetail> creditDetails;

	private List<CustomerDiscount> discounts;

	private Customer parent;

	public String getAddress() {
		return street() + barangay() + city() + province();
	}

	public Route getRoute() {
		return getRoute(LocalDate.now());
	}

	public Route getRoute(LocalDate date) {
		return routeHistory == null || routeHistory.isEmpty() ? null : route(date);
	}

	public String getSeller(LocalDate date) {
		Route route = getRoute(date);
		return route == null ? null : route.getSeller(date);
	}

	@Override
	public String toString() {
		return name;
	}

	private String barangay() {
		if (barangay == null)
			return "";
		return (street != null ? ", " : "") + barangay;
	}

	private String city() {
		if (city == null)
			return "";
		return (barangay != null || street != null ? ", " : "") + city;
	}

	private String province() {
		if (province == null)
			return "";
		return (city != null || barangay != null || street != null ? ", " : "") + province;
	}

	private Route route(LocalDate date) {
		return routeHistory.stream().filter(p -> p.getStartDate().compareTo(date) <= 0).max(Routing::compareTo).get()
				.getRoute();
	}

	private String street() {
		return street == null ? "" : street;
	}
}
