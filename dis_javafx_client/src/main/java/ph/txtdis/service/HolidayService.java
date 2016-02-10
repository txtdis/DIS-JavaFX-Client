package ph.txtdis.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import ph.txtdis.dto.Holiday;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Scope("prototype")
@Service("holidayService")
public class HolidayService implements Iconed, Listed<Holiday> {

	@Autowired
	private ReadOnlyService<Holiday> readOnlyService;

	@Autowired
	private SavingService<Holiday> savingService;

	@Override
	public String getModule() {
		return "holiday";
	}

	@Override
	public ReadOnlyService<Holiday> getReadOnlyService() {
		return readOnlyService;
	}

	public boolean isAHoliday(LocalDate d) {
		if (d != null)
			try {
				return holiday(d) != null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return true;
	}

	public Holiday save(LocalDate date, String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		Holiday h = new Holiday();
		h.setDeclaredDate(date);
		h.setName(name);
		return savingService.module(getModule()).save(h);
	}

	public void validateDate(LocalDate d) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException, DuplicateException {
		if (d == null)
			return;
		if (holiday(d) != null)
			throw new DuplicateException(toDateDisplay(d));
	}

	private Holiday holiday(LocalDate d) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/find?date=" + d);
	}
}
