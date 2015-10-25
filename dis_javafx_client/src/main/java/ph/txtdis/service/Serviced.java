package ph.txtdis.service;

import ph.txtdis.dto.Keyed;

public interface Serviced<T extends Keyed<PK>, PK> extends GetSet<PK>, Keyed<PK>, Saved<T, PK> {

	T find(String id) throws Exception;
}
