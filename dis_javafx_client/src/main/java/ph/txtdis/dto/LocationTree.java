package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationTree extends AbstractTrackedId<Long> {

	private Location location;

	private Location parent;
}
