package ph.txtdis.service;

import java.time.LocalDate;

import ph.txtdis.dto.ApprovalNeeded;

public interface ServiceWithApprovalNeeded {

	default boolean isApprovedAndStartDateIsNotInTheFuture(ApprovalNeeded e, LocalDate d) {
		return e.getApproved() != null && e.getApproved() && e.getStartDate().compareTo(d) <= 0;
	}
}
