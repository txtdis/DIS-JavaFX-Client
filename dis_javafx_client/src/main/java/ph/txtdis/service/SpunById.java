package ph.txtdis.service;

import ph.txtdis.dto.Audited;

public interface SpunById<PK> extends Audited, Spun {

	PK getSpunId();

	boolean isNew();
}
