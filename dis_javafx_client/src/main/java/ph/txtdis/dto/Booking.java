package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends AbstractSoldOrder<Long> {

	private String printedBy;

	private ZonedDateTime printedOn;

	public String getCustomerName() {
		return getCustomer() == null ? null : getCustomer().getName();
	}

	public String getLocation() {
		return getCustomer() == null ? null : getCustomer().getBarangay() + ", " + getCustomer().getProvince();
	}

	@Override
	public String toString() {
		return "S/O No. " + getId();
	}
}
