package ph.txtdis.service;

import java.time.ZonedDateTime;

public interface NeededDecisionDisplayed extends DecisionNeeded {

	String getDecidedBy();

	ZonedDateTime getDecidedOn();

	Boolean getIsValid();

	String getRemarks();
}
