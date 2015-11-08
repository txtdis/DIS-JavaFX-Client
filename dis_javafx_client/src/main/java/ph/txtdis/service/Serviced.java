package ph.txtdis.service;

import java.time.LocalDate;

import ph.txtdis.dto.Keyed;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.util.DateTimeUtils;

public interface Serviced<T extends Keyed<PK>, PK> extends AlternateNamed, SpunById<PK>, Saved<T, PK> {

	default T find(LocalDate d) throws Exception {
		T e = getReadOnlyService().module(getSpunModule()).getOne("/date?on=" + d);
		if (e == null)
			throw new NotFoundException(getHeaderText() + " dated " + DateTimeUtils.toDateDisplay(d));
		return e;
	}

	default T find(String id) throws Exception {
		T e = getReadOnlyService().module(getModule()).getOne("/" + id);
		if (e == null)
			throw new NotFoundException(getModuleId() + id);
		return e;
	}

	ReadOnlyService<T> getReadOnlyService();

	default void open(LocalDate d) throws Exception {
		set(find(d));
	}

	default void open(String id) throws Exception {
		set(find(id));
	}
}
