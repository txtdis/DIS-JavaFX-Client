package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Picking extends AbstractTrackedId<Long> {

	private Truck truck;

	private User driver;

	private User leadHelper;

	private User asstHelper;

	private LocalDate pickDate;

	private String remarks;

	private List<Booking> bookings;

	private User printedBy;

	private ZonedDateTime printedOn;
}
