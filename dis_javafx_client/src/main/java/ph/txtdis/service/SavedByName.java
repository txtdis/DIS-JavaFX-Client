package ph.txtdis.service;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;

public interface SavedByName<T> {

	T save(String name)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException;
}
