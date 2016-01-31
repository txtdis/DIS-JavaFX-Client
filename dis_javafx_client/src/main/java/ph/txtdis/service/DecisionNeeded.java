package ph.txtdis.service;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface DecisionNeeded {

	boolean canApprove();

	default boolean closeAppIfInvalid() {
		return false;
	}

	void saveDecision() throws SuccessfulSaveInfo, NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException;

	void updatePerValidity(Boolean isValid, String remarks) throws SuccessfulSaveInfo, NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException;
}
