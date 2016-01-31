package ph.txtdis.service;

import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

public interface UniquelyNamed<T> extends Moduled {

	default void confirmUniqueness(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, DuplicateException, RestException {
		if (findByName(name) != null)
			throw new DuplicateException(name);
	}

	default T findByName(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return getReadOnlyService().module(getModule()).getOne("/" + name);
	}

	ReadOnlyService<T> getReadOnlyService();
}
