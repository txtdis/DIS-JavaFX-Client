package ph.txtdis.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Audited;
import ph.txtdis.dto.Picking;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.info.SuccessfulSaveInfo;

@Service("pickService")
public class PickService implements Audited, Reset, Serviced<Picking, Long> {

	private static final String PICKING = "picking";

	@Autowired
	private ReadOnlyService<Picking> readOnlyService;

	@Autowired
	private SavingService<Picking> savingService;

	private Picking pick;

	public PickService() {
		reset();
	}

	@Override
	public Picking find(String id) throws Exception {
		Picking c = readOnlyService.module(PICKING).getOne("/" + id);
		if (c == null)
			throw new NotFoundException("Pick List No. " + id);
		return c;
	}

	@Override
	public Picking get() {
		return pick;
	}

	public Picking getByBooking(Long id) throws Exception {
		return readOnlyService.module(PICKING).getOne("/booking?id=" + id);
	}

	@Override
	public String getCreatedBy() {
		return pick.getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return pick.getCreatedOn();
	}

	@Override
	public Long getId() {
		return pick.getId();
	}

	@Override
	public void reset() {
		pick = new Picking();
	}

	@Override
	public void save() throws Exception, SuccessfulSaveInfo {
		set(savingService.module(PICKING).save(pick));
	}

	@Override
	public void set(Picking entity) {
		pick = entity;
	}

	public void setOrderDateUponValidation(LocalDate value) {
		// TODO Auto-generated method stub

	}

	public void updateUponBookingIdValidation(long id) {
		if (id != 0) {
			// ensureBookingIdExists(id);
			// verifyBookingHasNotBeenReferenced(id);
			// confirmBookingHasBeenPicked(id);
			// setAsBookedOrder(id);
			// netReturnedItems(id);
		}
	}
}
