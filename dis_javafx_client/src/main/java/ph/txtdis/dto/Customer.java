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
public class Customer extends AbstractTrackedId<Long> {

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

	private List<Discount> discounts;

	private Customer parent;

	public String getAddress() {
		return street() + barangay() + city() + province();
	}

	public CreditDetail getCredit(LocalDate date) {
		try {
			return getCreditDetails().stream().filter(p -> !p.getStartDate().isAfter(date))
					.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get();
		} catch (Exception e) {
			return null;
		}
	}

	public Route getRoute() {
		return getRoute(LocalDate.now());
	}

	public Route getRoute(LocalDate date) {
		try {
			return getRouteHistory().stream().filter(p -> !p.getStartDate().isAfter(date))
					.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get().getRoute();
		} catch (Exception e) {
			return null;
		}
	}

	public String getSeller(LocalDate date) {
		try {
			return getRoute(date).getSeller(date);
		} catch (Exception e) {
			return null;
		}
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

	private String street() {
		return street == null ? "" : street;
	}
}
