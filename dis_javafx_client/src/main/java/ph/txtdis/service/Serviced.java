package ph.txtdis.service;

import java.time.LocalDate;

import ph.txtdis.dto.Keyed;
import ph.txtdis.exception.DeactivatedException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.NoVendorIdPurchasedItemException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.util.DateTimeUtils;

public interface Serviced<PK> extends AlternateNamed, Iconed, SpunById<PK>, Saved<PK> {

	@SuppressWarnings("unchecked")
	default <T> T find(LocalDate d) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, RestException {
		T e = (T) getReadOnlyService().module(getSpunModule()).getOne("/date?on=" + d);
		if (e == null)
			throw new NotFoundException(getHeaderText() + " dated " + DateTimeUtils.toDateDisplay(d));
		return e;
	}

	default <T> T find(Long id)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			NotFoundException, DeactivatedException, RestException, NoVendorIdPurchasedItemException {
		return find(id.toString());
	}

	@SuppressWarnings("unchecked")
	default <T> T find(String id) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, DeactivatedException, RestException {
		String endpt = "/" + (getSpunModule().equals(getModule()) ? id : getSpunModule() + "?id=" + id);
		T e = (T) getReadOnlyService().module(getModule()).getOne(endpt);
		if (e == null)
			throw new NotFoundException(getModuleId() + id);
		return e;
	}

	<T extends Keyed<PK>> ReadOnlyService<T> getReadOnlyService();

	default void open(LocalDate d) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, RestException {
		set(find(d));
	}

	default void open(Long id)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			NotFoundException, DeactivatedException, NoVendorIdPurchasedItemException, RestException {
		set(find(id));
	}

	default void open(String id) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, NotFoundException, DeactivatedException, RestException {
		set(find(id));
	}
}
