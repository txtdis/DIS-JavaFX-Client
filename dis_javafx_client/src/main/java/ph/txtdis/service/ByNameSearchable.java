package ph.txtdis.service;

import java.util.List;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

public interface ByNameSearchable<T> {

	List<T> search(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException;
}
