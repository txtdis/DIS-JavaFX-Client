package ph.txtdis.service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface Saved<T extends Keyed<PK>, PK> extends Moduled, GetSet<PK> {

	SavingService<T> getSavingService();

	@SuppressWarnings("unchecked")
	default void save() throws Exception, SuccessfulSaveInfo {
		set(getSavingService().module(getModule()).save((T) get()));
		if (get() != null)
			throw new SuccessfulSaveInfo(get());
	}
}
