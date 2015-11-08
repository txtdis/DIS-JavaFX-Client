package ph.txtdis.service;

import java.time.ZonedDateTime;

public interface Audited {

	String getAuditedBy();

	ZonedDateTime getAuditedOn();

	Boolean getIsValid();

	String getRemarks();

	void setIsValid(Boolean isValid);

	void setRemarks(String s);

	void updatePerValidity(Boolean b);
}
