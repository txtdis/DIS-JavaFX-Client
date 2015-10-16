package ph.txtdis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Delivery;

@Service
public class DeliveryService extends BookedService<Delivery, Long> implements Reset {

	@Autowired
	private InvoiceService invoiceService;

	public DeliveryService() {
		reset();
	}

	@Override
	public String getAlternateName() {
		return "D/R";
	}

	@Override
	public String getModule() {
		return "delivery";
	}

	@Override
	public void reset() {
		set(new Delivery());
	}

	@Override
	protected void verifyBookingHasNotBeenReferenced(Long id) throws Exception {
		super.verifyBookingHasNotBeenReferenced(id);
		invoiceService.verifyNoInvoiceReferencedBooking(id);
	}

	protected void verifyNoDeliveryReportReferencedBooking(Long id) throws Exception {
		super.verifyBookingHasNotBeenReferenced(id);
	}
}
