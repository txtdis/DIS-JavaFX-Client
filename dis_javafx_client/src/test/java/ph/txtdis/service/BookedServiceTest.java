package ph.txtdis.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ph.txtdis.dto.AbstractBookedOrder;
import ph.txtdis.exception.AlreadyReferencedBookingIdException;
import ph.txtdis.exception.InvalidDateSequenceException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.NotPickedBookingIdException;
import ph.txtdis.type.UomType;
import ph.txtdis.util.Temporal;

public class BookedServiceTest extends TestData {

	private BookedService<AbstractBookedOrder<Long>, Long> service;
	private BookingService bookingService;
	private PickService pickService;
	private ReadOnlyService<AbstractBookedOrder<Long>> readOnlyService;
	private ReceivingService receivingService;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		bookingService = Mockito.mock(BookingService.class);
		pickService = Mockito.mock(PickService.class);
		readOnlyService = Mockito.mock(ReadOnlyService.class);
		receivingService = Mockito.mock(ReceivingService.class);
		service = new BookedService<AbstractBookedOrder<Long>, Long>(readOnlyService, bookingService, pickService,
				receivingService)
		{
			@Override
			public String getAlternateName() {
				return "TEST";
			}

			@Override
			public String getModule() {
				return "test";
			}
		};
		service.set(getBookedOrder());
		when(readOnlyService.module(service.getModule())).thenReturn(readOnlyService);
	}

	@Test
	public void testGetBalance() {
		getEntity().setUnpaidValue(getUnitPriceOf23p48());
		assertTrue(getUnitPriceOf23p48().equals(service.getBalance()));
	}

	@Test
	public void testGetBookingId() {
		assertNull(service.getBookingId());
		getEntity().setBooking(getBookingOfBareCustomer());
		assertSame(getBookingOfBareCustomer().getId(), service.getBookingId());
	}

	@Test
	public void testGetPaymentListWhenNone() {
		getEntity().setPayments(null);
		assertTrue(service.getPaymentList().isEmpty());
	}

	@Test
	public void testGetPaymentListWhenOne() {
		getEntity().setPayments(getRemittancePaymentListOf369p60DatedNow());
		String s = "[R/S No. 2 - " + Temporal.format(getDateOfNow()) + "] " + "₱369.60";
		assertTrue(Arrays.asList(s).equals(service.getPaymentList()));
	}

	@Test
	public void testGetPaymentListWhenTwo() {
		getEntity().setPayments(getRemittancePaymentListOf100by369p60DatedNow());
		String t = "[TOTAL] ₱469.60";
		String s1 = "[R/S No. 1 - " + Temporal.format(getDateOfNow()) + "] " + "₱100.00";
		String s2 = "[R/S No. 2 - " + Temporal.format(getDateOfNow()) + "] " + "₱369.60";
		assertTrue(Arrays.asList(t, s1, s2).equals(service.getPaymentList()));
	}

	@Test
	public void testResetBooking() throws Exception {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setBooking(getBookingOfBareCustomer());
		getEntity().setCustomer(getCustomerOfBareType());
		getEntity().setCredit(getCreditDetailOf7Days0Grace10kLimitOldDate());
		getEntity().setRemarks("TEST");
		getEntity().setRoute(getRouteOfS41());
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setDiscounts(getDiscountListOf10());
		service.resetBooking();
		assertTrue(getDateOfNow().isEqual(getEntity().getOrderDate()));
		assertNoBookingDataExtracted();
	}

	@Test
	public void testSetDetailsThatTheTotalValueEqualsBalance() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		assertTrue(getUnitPriceOf23p48().multiply(BigDecimal.TEN).compareTo(service.getBalance()) == 0);
	}

	@Test(expected = NotPickedBookingIdException.class)
	public void testUpdateUponBookingIdValidationWhenBookingHasNotBeenPicked() throws Exception {
		when(bookingService.find("1")).thenReturn(getBookingOfBareCustomer());
		when(readOnlyService.getOne("/booking?id=" + 1L)).thenReturn(null);
		when(pickService.getByBooking(1L)).thenReturn(null);
		service.updateUponBookingIdValidation(1);
	}

	@Test(expected = AlreadyReferencedBookingIdException.class)
	public void testUpdateUponBookingIdValidationWhenIdIsAlreadyReferenced() throws Exception {
		when(bookingService.find("1")).thenReturn(getBookingOfBareCustomer());
		when(readOnlyService.getOne("/booking?id=" + 1L)).thenReturn(getBookedOrder());
		service.updateUponBookingIdValidation(1L);
	}

	@Test(expected = NotFoundException.class)
	public void testUpdateUponBookingIdValidationWhenIdIsNotFound() throws Exception {
		when(bookingService.find("-1")).thenReturn(null);
		service.updateUponBookingIdValidation(-1);
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndAllItemsAreReturned() throws Exception {
		getEntity().setOrderDate(getDateOfNow());
		setUpBookingDatedNowForBareCustomerWithPicked10PiecePineFlat();
		when(receivingService.find(1L)).thenReturn(getReceivingOfBareCustomerWith10PiecesPineSlice());
		service.updateUponBookingIdValidation(1);
		assertSame(0, service.getDetails().size());
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndAVolumeDiscountedItemReturnedQtyIsACase()
			throws Exception {
		setUpBookingDatedNowForBareCustomerWithPickedPerPieceAndPerCaseAndVolumeDiscountedItems();
		when(receivingService.find(1L)).thenReturn(getReceivingOfBareCustomerWithOneCasePineSlice());
		service.updateUponBookingIdValidation(1);
		assertSame(2, service.getDetails().size());
		assertSame(UomType.CS, service.getDetails().get(0).getUom());
		assertTrue(new BigDecimal("9").compareTo(service.getDetails().get(0).getQty()) == 0);
		assertTrue(new BigDecimal("562.52")
				.equals(service.getDetails().get(0).getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndAVolumeDiscountedItemReturnedQtyIsLessThanACase()
			throws Exception {
		setUpBookingDatedNowForBareCustomerWithPickedPerPieceAndPerCaseAndVolumeDiscountedItems();
		when(receivingService.find(1L)).thenReturn(getReceivingOfBareCustomerWith10PiecesPineSlice());
		service.updateUponBookingIdValidation(1);
		assertSame(2, service.getDetails().size());
		assertSame(UomType.PC, service.getDetails().get(0).getUom());
		assertTrue(new BigDecimal("230").compareTo(service.getDetails().get(0).getQty()) == 0);
		assertTrue(new BigDecimal("23.44")
				.equals(service.getDetails().get(0).getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndNonVolumeDiscountedItemQtyArePartiallyReturned()
			throws Exception {
		setUpBookingDatedNowForBareCustomerWithPickedPerPieceAndPerCaseAndVolumeDiscountedItems();
		when(receivingService.find(1L)).thenReturn(getReceivingOfBareCustomerWith10PiecesTjReg1kPeBag());
		service.updateUponBookingIdValidation(1);
		assertSame(2, service.getDetails().size());
		assertTrue(BigDecimal.TEN.compareTo(service.getDetails().get(0).getQty()) == 0);
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndNoReturns() throws Exception {
		getEntity().setOrderDate(getDateOfNow());
		setUpBookingDatedNowForBareCustomerWithPicked10PiecePineFlat();
		when(receivingService.find(1L)).thenThrow(new NotFoundException(""));
		service.updateUponBookingIdValidation(1);
		assertSame(getBookingOfBareCustomer().getId(), service.getBookingId());
		assertSame(1, service.getDetails().size());
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsValidAndOneItemQtyAreAllReturned() throws Exception {
		setUpBookingDatedNowForBareCustomerWithPickedPerPieceAndPerCaseAndVolumeDiscountedItems();
		when(receivingService.find(1L)).thenReturn(getReceivingOfBareCustomerWith20PiecesTjReg1kPeBag());
		service.updateUponBookingIdValidation(1);
		assertSame(1, service.getDetails().size());
	}

	@Test
	public void testUpdateUponBookingIdValidationWhenIdIsZero() throws Exception {
		service.updateUponBookingIdValidation(0);
		assertNoBookingDataExtracted();
	}

	@Test(expected = InvalidDateSequenceException.class)
	public void testUpdateUponBookingIdValidationWhenPickingIsAfterOrderDate() throws Exception {
		getEntity().setOrderDate(getDateOf20150101());
		setUpBookingDatedNowForBareCustomerWithPicked10PiecePineFlat();
		service.updateUponBookingIdValidation(1);
	}

	private void assertNoBookingDataExtracted() {
		assertNull(service.getBookingId());
		assertNull(getEntity().getCustomer());
		assertNull(service.getCredit());
		assertNull(getEntity().getRemarks());
		assertNull(getEntity().getRoute());
		assertTrue(service.getDetails().isEmpty());
		assertTrue(service.getDiscountTextList().isEmpty());
	}

	private AbstractBookedOrder<Long> getEntity() {
		return service.get();
	}

	private void setUpBookingDatedNowForBareCustomerWithPicked10PiecePineFlat() throws Exception {
		when(bookingService.find("1")).thenReturn(getBookingOfBareCustomer());
		when(readOnlyService.getOne("/booking?id=" + 1L)).thenReturn(null);
		when(pickService.getByBooking(1L)).thenReturn(getPicking());
	}

	private void setUpBookingDatedNowForBareCustomerWithPickedPerPieceAndPerCaseAndVolumeDiscountedItems()
			throws Exception {
		getEntity().setOrderDate(getDateOfNow());
		when(bookingService.find("1"))
				.thenReturn(getBookingOfBareCustomerWithPerPieceAndPerCaseAndVolumeDiscountedItems());
		when(readOnlyService.getOne("/booking?id=" + 1L)).thenReturn(null);
		when(pickService.getByBooking(1L)).thenReturn(getPicking());
	}
}
