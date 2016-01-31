package ph.txtdis.dto;

import java.time.ZonedDateTime;

public interface CreationTracked {

	String getCreatedBy();

	ZonedDateTime getCreatedOn();
}
