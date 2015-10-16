package ph.txtdis.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Routing extends AbstractTrackedId<Long>implements Comparable<Routing>, StartDated {

	private Route route;

	private LocalDate startDate;

	@Override
	public int compareTo(Routing routing) {
		return getStartDate().compareTo(routing.getStartDate());
	}
}
