package ph.txtdis.service;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import ph.txtdis.dto.Billable;

public class InvoiceServiceTest {

	private InvoiceService service;

	@Before
	public void setUp() throws Exception {
		// service = new InvoiceService();
	}

	@Test
	public void testGetAlternateName() {
		assertSame("S/I", service.getAlternateName());
	}

	@Test
	public void testReset() {
		service.reset();
		assertSame(new Billable(), service.get());
	}

	@Test
	public void testUpdateUponInvoiceIdValidation() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyBookingHasNotBeenReferenced() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyNoInvoiceReferencedBooking() {
		fail("Not yet implemented");
	}
}
