package ph.txtdis.service;

import org.springframework.stereotype.Service;

import ph.txtdis.dto.Booking;

@Service
public class BookingService extends SoldService<Booking, Long> implements AlternateNamed, Reset {

	public BookingService() {
		reset();
	}

	@Override
	public String getAlternateName() {
		return "S/O";
	}

	@Override
	public String getModule() {
		return "booking";
	}

	@Override
	public void reset() {
		set(new Booking());
	}
}
