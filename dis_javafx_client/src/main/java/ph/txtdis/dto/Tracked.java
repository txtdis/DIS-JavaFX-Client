package ph.txtdis.dto;

import java.time.ZonedDateTime;

public interface Tracked {

	String getCreatedBy();

	ZonedDateTime getCreatedOn();
}
