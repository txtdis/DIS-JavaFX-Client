package ph.txtdis.dto;

import java.time.ZonedDateTime;

public interface Audited {

	String getCreatedBy();

	ZonedDateTime getCreatedOn();
}
