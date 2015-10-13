package ph.txtdis.service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface Serviced<T, PK> extends Keyed<PK> {

	T find(String id) throws Exception;

	T get();

	void save() throws Exception, SuccessfulSaveInfo;

	void set(T entity);
}
