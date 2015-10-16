package ph.txtdis.service;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.Picking;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.info.SuccessfulSaveInfo;

@Service("pickService")
public class PickService implements AlternateNamed, Reset, Serviced<Picking, Long>, SpunById<Long> {

	@Autowired
	private ReadOnlyService<Picking> readOnlyService;

	@Autowired
	private SavingService<Picking> savingService;

	@Autowired
	private SpunService<Picking, Long> spunService;

	private Picking pick;

	public PickService() {
		reset();
	}

	@Override
	public Picking find(String id) throws Exception {
		Picking c = readOnlyService.module(getModule()).getOne("/" + id);
		if (c == null)
			throw new NotFoundException(getAlternateName() + "No. " + id);
		return c;
	}

	@Override
	public Picking get() {
		return pick;
	}

	@Override
	public String getAlternateName() {
		return "Pick List";
	}

	public Picking getByBooking(Long id) throws Exception {
		return readOnlyService.module(getModule()).getOne("/booking?id=" + id);
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
	public String getModule() {
		return "picking";
	}

	@Override
	public SpunService<Picking, Long> getSpunService() {
		return spunService;
	}

	@Override
	public void next() throws Exception {
		set(spunService.module(getModule()).next(getSpunId()));
	}

	@Override
	public void previous() throws Exception {
		set(spunService.module(getModule()).previous(getSpunId()));
	}

	@Override
	public void reset() {
		pick = new Picking();
	}

	@Override
	public void save() throws Exception, SuccessfulSaveInfo {
		set(savingService.module(getModule()).save(pick));
	}

	@Override
	public void set(Keyed<Long> entity) {
		pick = (Picking) entity;
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
