package ph.txtdis.service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Tracked;

public interface SpunById<PK> extends Tracked, GetSet<PK>, Keyed<PK>, Moduled, Spun {

	default PK getSpunId() {
		return isNew() ? null : getId();
	}

	default String getSpunModule() {
		return getModule();
	}

	SpunService<? extends Keyed<PK>, PK> getSpunService();

	default boolean isNew() {
		return getCreatedBy() == null;
	}

	@Override
	default void next() throws Exception {
		set(getSpunService().module(getSpunModule()).next(getSpunId()));
	}

	@Override
	default void previous() throws Exception {
		set(getSpunService().module(getSpunModule()).previous(getSpunId()));
	}
}
