package ph.txtdis.dto;

import java.time.ZonedDateTime;

public interface ModificationTracked {

	String getDeactivatedBy();

	ZonedDateTime getDeactivatedOn();

	String getLastModifiedBy();

	ZonedDateTime getLastModifiedOn();

	void setDeactivatedBy(String u);

	void setDeactivatedOn(ZonedDateTime zdt);

	void setLastModifiedBy(String u);

	void setLastModifiedOn(ZonedDateTime zdt);
}
