package ph.txtdis.service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface Saved<PK> extends AlternateNamed, GetSet<PK>, Moduled {

	default String getOrderNo() {
		PK id = get().getId();
		return id == null ? null : id.toString();
	}

	<T> SavingService<T> getSavingService();

	@SuppressWarnings("unchecked")
	default <T extends Keyed<PK>> void save() throws SuccessfulSaveInfo, NoServerConnectionException,
			StoppedServerException, FailedAuthenticationException, InvalidException, RestException {
		set((T) getSavingService().module(getModule()).save(get()));
		if (get() != null)
			throw new SuccessfulSaveInfo(getModuleId() + getOrderNo());
	}
}
