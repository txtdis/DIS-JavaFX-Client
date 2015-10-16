package ph.txtdis.service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.info.SuccessfulSaveInfo;

public interface Serviced<T, PK> extends GetSet<PK>, Keyed<PK> {

	T find(String id) throws Exception;

	void save() throws Exception, SuccessfulSaveInfo;
}
