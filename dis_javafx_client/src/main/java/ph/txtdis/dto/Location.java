package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.LocationType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Location extends AbstractTrackedId<Long> {

	private String name;

	private LocationType type;

	@Override
	public String toString() {
		return name;
	}
}
