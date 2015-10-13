package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends AbstractSoldOrder<Long> {

	private User printedBy;

	private ZonedDateTime printedOn;

	public String getBarangay() {
		return getCustomer() == null ? null : getCustomer().getBarangay().toString();
	}

	public String getCustomerName() {
		return getCustomer() == null ? null : getCustomer().getName();
	}

	@Override
	public String toString() {
		return "S/O No. " + getId();
	}
}
