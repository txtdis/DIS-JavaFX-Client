package ph.txtdis.service;

import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;

public interface SavedByEntity<T> extends Moduled {

	SavingService<T> getSavingService();

	default T save(T entity) throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException,
			InvalidException {
		return getSavingService().module(getModule()).save(entity);
	}
}
