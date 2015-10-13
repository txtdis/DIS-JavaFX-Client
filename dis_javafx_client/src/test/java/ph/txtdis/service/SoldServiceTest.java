package ph.txtdis.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ph.txtdis.dto.AbstractSoldOrder;
import ph.txtdis.dto.Discount;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.SoldDetail;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.DifferentDiscountException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.NotAnItemToBeSoldToCustomerException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

public class SoldServiceTest extends TestData {

	private ItemFamilyService familyService;
	private ItemService itemService;
	private ReadOnlyService<AbstractSoldOrder<Long>> readOnlyService;
	private SavingService<AbstractSoldOrder<Long>> savingService;
	private SoldService<AbstractSoldOrder<Long>, Long> service;
	private SpunService<AbstractSoldOrder<Long>, Long> spunService;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() throws Exception {
		familyService = mock(ItemFamilyService.class);
		itemService = mock(ItemService.class);
		readOnlyService = mock(ReadOnlyService.class);
		savingService = mock(SavingService.class);
		spunService = mock(SpunService.class);
		service = new SoldService<AbstractSoldOrder<Long>, Long>(familyService, itemService, readOnlyService,
				savingService, spunService)
		{
		};
		service.set(getBookedOrder());
		when(readOnlyService.module(service.getModule())).thenReturn(readOnlyService);
		when(spunService.module(service.getModule())).thenReturn(spunService);
		when(savingService.module(service.getModule())).thenReturn(savingService);
	}

	@Test(expected = DuplicateException.class)
	public void testCheckforDuplicatesWhenItExists() throws Exception {
		when(readOnlyService.getOne("/1")).thenReturn(getBookedOrder());
		service.checkforDuplicates("1");
	}

	@Test
	public void testCheckforDuplicatesWhenNoneExists() throws Exception {
		when(readOnlyService.getOne("/1")).thenReturn(null);
		service.checkforDuplicates("1");
	}

	@Test
	public void testCreateDetailWhenSetVolumeDiscountInCasesForSupermarketIsReached() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfSupermarketType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSliceFlat());
		SoldDetail detail = service.createDetail(UomType.CS, BigDecimal.ONE, QualityType.GOOD);
		assertTrue(new BigDecimal("561.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenSetVolumeDiscountInCasesIsReached() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfBareType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSliceFlat());
		SoldDetail detail = service.createDetail(UomType.CS, BigDecimal.ONE, QualityType.GOOD);
		assertTrue(new BigDecimal("562.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenSetVolumeDiscountInPiecesIsExceeded() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfBareType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSliceFlat());
		SoldDetail detail = service.createDetail(UomType.PC, new BigDecimal("36"), QualityType.GOOD);
		assertTrue(new BigDecimal("23.45").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenSetVolumeDiscountInPiecesIsNotReached() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfBareType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSliceFlat());
		SoldDetail detail = service.createDetail(UomType.PC, BigDecimal.ONE, QualityType.GOOD);
		assertTrue(new BigDecimal("23.48").compareTo(detail.getPriceValue()) == 0);
	}

	@Test
	public void testCreateDetailWhenTierVolumeDiscountInCases1stLevelIsExceeded() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfBareType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSlice15());
		SoldDetail detail = service.createDetail(UomType.CS, new BigDecimal("15"), QualityType.GOOD);
		assertTrue(new BigDecimal("553.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenTierVolumeDiscountInCases2ndLevelIsExceeded() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfBareType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSlice15());
		SoldDetail detail = service.createDetail(UomType.CS, new BigDecimal("25"), QualityType.GOOD);
		assertTrue(new BigDecimal("543.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenTierVolumeDiscountInCasesForSupermarketIsExceeded() {
		service.get().setOrderDate(getDateOfNow());
		service.get().setCustomer(getCustomerOfSupermarketType());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSlice15());
		SoldDetail detail = service.createDetail(UomType.CS, new BigDecimal("25"), QualityType.GOOD);
		assertTrue(new BigDecimal("533.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenTierVolumeDiscountInCasesIsDiscountinued() {
		service.get().setOrderDate(getDateOfAMonthFromNow());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSlice15());
		SoldDetail detail = service.createDetail(UomType.CS, new BigDecimal("25"), QualityType.GOOD);
		assertTrue(new BigDecimal("563.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testCreateDetailWhenTierVolumeDiscountInCasesIsNotReached() {
		service.get().setOrderDate(getDateOfNow());
		service.setUnitPrice(getUnitPriceOf23p48());
		service.setItem(getItemPineSlice15());
		SoldDetail detail = service.createDetail(UomType.CS, new BigDecimal("9"), QualityType.GOOD);
		assertTrue(new BigDecimal("563.52").equals(detail.getPriceValue().setScale(2, RoundingMode.HALF_EVEN)));
	}

	@Test
	public void testFindWhenItExists() throws Exception {
		when(readOnlyService.getOne("/1")).thenReturn(getBookedOrder());
		service.find("1");
	}

	@Test(expected = NotFoundException.class)
	public void testFindWhenNoneExists() throws Exception {
		when(readOnlyService.getOne("/1")).thenReturn(null);
		service.find("1");
	}

	@Test
	public void testGet() {
		service.set(getBookedOrder());
		assertTrue(getBookedOrder().equals(getEntity()));
	}

	@Test
	public void testGetCreatedBy() {
		assertNull(service.getCreatedBy());
		String t = "TEST";
		getEntity().setCreatedBy(t);
		assertSame(t, service.getCreatedBy());
	}

	@Test
	public void testGetCreatedOn() {
		assertNull(service.getCreatedOn());
		ZonedDateTime t = ZonedDateTime.now();
		getEntity().setCreatedOn(t);
		assertSame(t, service.getCreatedOn());
	}

	@Test
	public void testGetCreditEvenWhenItExistsOnAPostedOrderThatItIsNullAsPerOrderNotFromDatabase() {
		getEntity().setCreatedBy("TEST");
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertNull(service.getCredit());
	}

	@Test
	public void testGetCreditWhenItExistsOnANewOrderThatItIsTheLatest() {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertTrue(getCreditDetailOf30Days7Grace100kLimitNowDate().equals(service.getCredit()));
	}

	@Test
	public void testGetCreditWhenNoneOnANewOrder() {
		assertNull(service.getCredit());
	}

	@Test
	public void testGetCustomerAddress() {
		assertNull(service.getCustomerAddress());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertTrue(getCustomerWithCreditAndDiscount().getAddress().equals(service.getCustomerAddress()));
	}

	@Test
	public void testGetCustomerId() {
		assertNull(service.getCustomerId());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertSame(getCustomerWithCreditAndDiscount().getId(), service.getCustomerId());
	}

	@Test
	public void testGetCustomerName() {
		assertNull(service.getCustomerName());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertSame("W/ CREDIT & DISCOUNT", service.getCustomerName());
	}

	@Test
	public void testGetDetails() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		assertSame(1, service.getDetails().size());
		service.setDetails(null);
		assertSame(0, service.getDetails().size());
	}

	@Test
	public void testGetDiscountTextListWhenDiscountedOf1Level() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setDiscounts(getDiscountListOf10());
		assertTrue(Arrays.asList("[10.00%] ₱23.48").equals(service.getDiscountTextList()));
	}

	@Test
	public void testGetDiscountTextListWhenDiscountedOf2Levels() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setDiscounts(getDiscountListOf10by5());
		String total = "[TOTAL] ₱34.05";
		String level1 = "[1- 10.00%] ₱23.48";
		String level2 = "[2- 5.00%] ₱10.57";
		assertTrue(Arrays.asList(total, level1, level2).equals(service.getDiscountTextList()));
	}

	@Test
	public void testGetDiscountTextListWhenNotDiscounted() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		assertSame(0, service.getDiscountTextList().size());
	}

	@Test
	public void testGetDiscountValueWhenDiscountExists() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setDiscounts(getDiscountListOf10by5());
		assertTrue(
				new BigDecimal("34.05").compareTo(service.getDiscountValue().setScale(2, RoundingMode.HALF_EVEN)) == 0);
	}

	@Test
	public void testGetDiscountValueWhenNoDiscountExists() {
		assertSame(BigDecimal.ZERO, service.getDiscountValue());
	}

	@Test
	public void testGetDueDateOnANewOrder() {
		assertNull(service.getDueDate());
	}

	@Test
	public void testGetDueDateWhenCustomerHasCreditUsingLatestTerm() {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertTrue(LocalDate.now().plusDays(30).isEqual(service.getDueDate()));
	}

	@Test
	public void testGetDueDateWhenCustomerHasCreditUsingSavedNotUpdatedTerm() {
		getEntity().setCreatedBy("TEST");
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		assertTrue(LocalDate.now().isEqual(service.getDueDate()));
	}

	@Test
	public void testGetDueDateWhenCustomerHasNoCredit() {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerOfBareType());
		assertTrue(LocalDate.now().isEqual(service.getDueDate()));
	}

	@Test
	public void testGetId() {
		assertNull(service.getId());
		Long t = 1L;
		getEntity().setId(t);
		assertSame(t, service.getId());
	}

	@Test
	public void testGetItemDescriptionWhenItemExists() {
		service.setItem(getItemPineSliceFlat());
		assertSame(getItemPineSliceFlat().getDescription(), service.getItemDescription());
	}

	@Test
	public void testGetItemDescriptionWhenNoItemExists() {
		assertNull(service.getItemDescription());
	}

	@Test
	public void testGetLatestDiscountWhenInFamilyLimitThatExcludesFutureStartDates() {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		List<Discount> tjFamilyDiscount = getEntityDiscountList1by0p5();
		assertTrue(tjFamilyDiscount.equals(service.getLatestDiscount(getFamilyListTj())));
	}

	@Test
	public void testGetLatestDiscountWhenNoneExists() {
		assertTrue(service.getLatestDiscount(getFamilyListTj()).isEmpty());
	}

	@Test
	public void testGetLatestDiscountWhenNotInFamilyLimitThatExcludesFutureStartDates() {
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		List<Discount> allFamilyDiscount = getEntityDiscountList0p1by0p05();
		assertTrue(allFamilyDiscount.equals(service.getLatestDiscount(getFamilyListPine())));
	}

	@Test
	public void testGetLatestDiscountWhenOrderDateIsInTheFutureAndInFamilyLimitOfPastButNotInTheFuture() {
		getEntity().setOrderDate(getDateOfAMonthFromNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		List<Discount> tjFamilyDiscount = getEntityDiscountList1by0p5();
		assertTrue(tjFamilyDiscount.equals(service.getLatestDiscount(getFamilyListTj())));
	}

	@Test
	public void testGetLatestDiscountWhenOrderDateIsInTheFutureAndNotInFamilyLimitOfPastButInTheFuture() {
		getEntity().setOrderDate(getDateOfAMonthFromNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		List<Discount> pineFamilyDiscount = getEntityDiscountList3by1p5();
		assertTrue(pineFamilyDiscount.equals(service.getLatestDiscount(getFamilyListPine())));
	}

	@Test
	public void testGetOrderDate() {
		assertNull(getEntity().getOrderDate());
		getEntity().setOrderDate(getDateOfNow());
		assertTrue(LocalDate.now().isEqual(getEntity().getOrderDate()));
	}

	@Test
	public void testGetRemarks() {
		assertNull(getEntity().getRemarks());
		getEntity().setRemarks("REMARKS");
		assertSame("REMARKS", getEntity().getRemarks());
	}

	@Test
	public void testGetSpunId() {
		getEntity().setId(1L);
		assertNull(service.getSpunId());
		getEntity().setCreatedBy("TEST");
		assertSame(1L, service.getSpunId());
	}

	@Test
	public void testGetTotalValueWhenDiscounted() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setDiscounts(getDiscountListOf10by5());
		assertTrue(new BigDecimal("200.754").compareTo(service.getTotalValue()) == 0);
	}

	@Test
	public void testGetTotalValueWhenNoDetails() {
		assertNull(service.getTotalValue());
	}

	@Test
	public void testGetTotalValueWhenNotDiscounted() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		assertTrue(new BigDecimal("234.80").equals(service.getTotalValue()));
	}

	@Test
	public void testGetVatableValue() throws Exception {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		service.setVatDivisor(getVatDivisorOf1p12());
		assertTrue(new BigDecimal("209.6429").compareTo(service.getVatableValue()) == 0);
	}

	@Test
	public void testGetVatableValueWhenNoDetails() throws Exception {
		assertNull(service.getVatableValue());
	}

	@Test
	public void testGetVatValue() throws Exception {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		service.setVatDivisor(getVatDivisorOf1p12());
		assertTrue(new BigDecimal("25.1571").equals(service.getVatValue()));
	}

	@Test
	public void testGetVatValueWhenNoDetails() throws Exception {
		assertNull(service.getVatValue());
	}

	@Test
	public void testIsNew() throws Exception {
		assertTrue(service.isNew());
		getEntity().setCreatedBy("TEST");
		assertFalse(service.isNew());
	}

	@Test
	public void testNext() throws Exception {
		when(spunService.next(service.getSpunId())).thenReturn(getBookedOrder());
		service.next();
		assertTrue(getBookedOrder().equals(getEntity()));
	}

	@Test
	public void testPrevious() throws Exception {
		when(spunService.previous(service.getSpunId())).thenReturn(getBookedOrder());
		service.previous();
		assertTrue(getBookedOrder().equals(getEntity()));
	}

	@Test(expected = SuccessfulSaveInfo.class)
	public void testSave() throws Throwable {
		service.save();
	}

	@Test
	public void testSet() {
		AbstractSoldOrder<Long> t = getBookedOrder();
		String s = "TEST";
		t.setCreatedBy(s);
		service.set(t);
		assertSame(s, service.getCreatedBy());
		assertNull(service.getCreatedOn());
	}

	@Test
	public void testSetDetails() {
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		assertTrue(getDetailsOfPineSliceByPieceBy10ByGood().equals(service.getDetails()));
	}

	@Test(expected = DifferentDiscountException.class)
	public void testSetItemUponValidationWhenDiscountsDifferThrowingException() throws Exception {
		when(itemService.find(3L)).thenReturn(getItemTjReg1kVac());
		getEntity().setOrderDate(getDateOfNow());
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		service.setItemUponValidation(3L);
	}

	@Test(expected = NotAnItemToBeSoldToCustomerException.class)
	public void testSetItemUponValidationWhenItCannotBeSoldToCashier() throws Exception {
		when(itemService.find(1L)).thenReturn(getItemPineSliceFlat());
		getEntity().setCustomer(getCustomerOfCashierType());
		getEntity().setOrderDate(getDateOfNow());
		service.setItemUponValidation(1L);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NotFoundException.class)
	public void testSetItemUponValidationWhenItDoesNotExists() throws Exception {
		when(itemService.find(-1L)).thenThrow(NotFoundException.class);
		service.setItemUponValidation(-1L);
	}

	@Test
	public void testSetItemUponValidationWhenItHasNoDiscountSameAsPreviousItems() throws Exception {
		when(itemService.find(3L)).thenReturn(getItemTjReg1kVac());
		getEntity().setOrderDate(getDateOfNow());
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		getEntity().setCustomer(getCustomerOfBareType());
		service.setItemUponValidation(3L);
		assertTrue(getItemTjReg1kVac().equals(service.getItem()));
	}

	@Test
	public void testSetItemUponValidationWhenItHasNoSupermarketPricingAndCustomerIsOfSupermarketType()
			throws Exception {
		when(itemService.find(1L)).thenReturn(getItemPineSliceFlat());
		getEntity().setCustomer(getCustomerOfSupermarketType());
		getEntity().setOrderDate(getDateOfNow());
		service.setItemUponValidation(1L);
		assertTrue(getPriceOfDealerTypeWorth23p48By20150101().getPriceValue().equals(service.getUnitPrice()));
	}

	@Test
	public void testSetItemUponValidationWhenItHasSupermarketPricing() throws Exception {
		when(itemService.find(3L)).thenReturn(getItemTjReg1kVac());
		getEntity().setCustomer(getCustomerOfSupermarketType());
		getEntity().setOrderDate(getDateOfNow());
		service.setItemUponValidation(3L);
		assertTrue(getPriceOfSupermarketTypeWorth110ByNow().getPriceValue().equals(service.getUnitPrice()));
	}

	@Test(expected = DuplicateException.class)
	public void testSetItemUponValidationWhenItIsAlreadyOnList() throws Exception {
		when(itemService.find(1L)).thenReturn(getItemPineSliceFlat());
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		service.setItemUponValidation(1L);
	}

	@Test
	public void testSetItemUponValidationWhenItIsFirstAddingItToListNotThrowingDiscountDiffersException()
			throws Exception {
		when(itemService.find(3L)).thenReturn(getItemTjReg1kVac());
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		service.setItemUponValidation(3L);
		assertTrue(getItemTjReg1kVac().equals(service.getItem()));
	}

	@Test
	public void testSetItemUponValidationWhenItIsValid() throws Exception {
		when(itemService.find(1L)).thenReturn(getItemPineSliceFlat());
		getEntity().setCustomer(getCustomerOfBareType());
		getEntity().setOrderDate(getDateOfNow());
		service.setItemUponValidation(1L);
		assertTrue(getPriceListOfOldPurchaseAndNewPurchaseAndOldDealer().equals(service.getItem().getPriceList()));
	}

	@Test
	public void testSetItemUponValidationWhenItsDiscountIsTheSameAsPreviousItems() throws Exception {
		getEntity().setDiscounts(getEntityDiscountList0p1by0p05());
		when(itemService.find(2L)).thenReturn(getItemPineSlice15());
		getEntity().setOrderDate(getDateOfNow());
		getEntity().setCustomer(getCustomerWithCreditAndDiscount());
		service.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		service.setItemUponValidation(2L);
		assertTrue(getItemPineSlice15().equals(service.getItem()));
	}

	@Test
	public void testSetOrderDateUponValidationWhenDateIsCurrent() throws Exception {
		service.setOrderDateUponValidation(getDateOfNow());
		assertTrue(getDateOfNow().equals(getEntity().getOrderDate()));
	}

	@Test(expected = DateInTheFutureException.class)
	public void testSetOrderDateUponValidationWhenDateIsInTheFuture() throws Exception {
		service.setOrderDateUponValidation(getDateOfAMonthFromNow());
	}

	@Test
	public void testSetOrderDateUponValidationWhenDateIsNull() throws Exception {
		service.setOrderDateUponValidation(null);
		assertNull(getEntity().getOrderDate());
	}

	@Test
	public void testSetRemarks() {
		String t = "TEST";
		getEntity().setRemarks(t);
		assertSame(t, getEntity().getRemarks());
	}

	@Test
	public void testSetRoute() {
		Route t = getRouteOfS41();
		getEntity().setRoute(t);
		assertSame(t, getEntity().getRoute());
	}

	@Test
	public void testSetSellingUoms() throws Exception {
		List<UomType> list = Arrays.asList(UomType.PC);
		service.setSellingUoms(list);
		assertSame(list, service.getSellingUoms());
	}

	@Test(expected = DateInTheFutureException.class)
	public void testUpdateUponDateValidationWhenDateIsInTheFutre() throws Exception {
		service.setOrderDateUponValidation(getDateOfAMonthFromNow());
	}

	@Test
	public void testUpdateUponDateValidationWhenDateIsNull() throws Exception {
		service.setOrderDateUponValidation(null);
		assertNull(getEntity().getOrderDate());
	}

	@Test
	public void testUpdateUponDateValidationWhenDateIsValid() throws Exception {
		service.setOrderDateUponValidation(getDateOfNow());
		assertTrue(getDateOfNow().isEqual(getEntity().getOrderDate()));
	}

	private AbstractSoldOrder<Long> getEntity() {
		return service.get();
	}
}
