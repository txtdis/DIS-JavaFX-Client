package ph.txtdis.service;

import static java.time.ZonedDateTime.now;
import static ph.txtdis.util.SpringUtil.username;

import java.time.ZonedDateTime;

import ph.txtdis.dto.ModificationTracked;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface ServiceDeactivated<PK> extends Saved<PK> {

	default void deactivate() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		((ModificationTracked) get()).setDeactivatedBy(username());
		((ModificationTracked) get()).setDeactivatedOn(now());
		save();
	}

	default String getDeactivatedBy() {
		return ((ModificationTracked) get()).getDeactivatedBy();
	}

	default ZonedDateTime getDeactivatedOn() {
		return ((ModificationTracked) get()).getDeactivatedOn();
	}
}
