package ph.txtdis.service;

import ph.txtdis.dto.Audited;
import ph.txtdis.dto.Keyed;

public interface SpunById<PK> extends Audited, GetSet<PK>, Keyed<PK>, Moduled, Spun {

	default PK getSpunId() {
		return isNew() ? null : getId();
	}

	SpunService<? extends Keyed<PK>, PK> getSpunService();

	default boolean isNew() {
		return getCreatedBy() == null;
	}

	@Override
	default void next() throws Exception {
		set(getSpunService().module(getModule()).next(getSpunId()));
	}

	@Override
	default void previous() throws Exception {
		set(getSpunService().module(getModule()).previous(getSpunId()));
	}
}
