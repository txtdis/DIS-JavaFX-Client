package ph.txtdis.service;

import ph.txtdis.dto.Keyed;

public interface GetSet<PK> {

	Keyed<PK> get();

	void set(Keyed<PK> entity);
}
