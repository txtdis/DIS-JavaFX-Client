package ph.txtdis.service;

import java.time.LocalDate;

import ph.txtdis.dto.Billable;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.exception.UnbilledPickedSalesOrderException;

public interface BilledAllPickedSalesOrder {

	ReadOnlyService<Billable> getBillableReadOnlyService();

	default void verifyAllPickedSalesOrderHaveBeenBilled(String seller, LocalDate date)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			RestException, UnbilledPickedSalesOrderException {
		Billable b = getBillableReadOnlyService().module("billable")
				.getOne("/unbilled?seller=" + seller + "&upTo=" + date);
		if (b != null)
			throw new UnbilledPickedSalesOrderException(b.getBookingId());
	}
}
