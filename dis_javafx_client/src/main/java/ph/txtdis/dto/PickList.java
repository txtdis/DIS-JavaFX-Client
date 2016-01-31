package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PickList extends EntityCreationTracked<Long> {

	private String truck, driver, leadHelper, asstHelper, remarks, printedBy;

	private LocalDate pickDate;

	private List<Booking> bookings;

	private ZonedDateTime printedOn;

	@Override
	public String toString() {
		return "Pick List No. " + getId();
	}
}
