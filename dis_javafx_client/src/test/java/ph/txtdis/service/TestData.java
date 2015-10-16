package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ph.txtdis.dto.AbstractBookedOrder;
import ph.txtdis.dto.Account;
import ph.txtdis.dto.Booking;
import ph.txtdis.dto.Channel;
import ph.txtdis.dto.CreditDetail;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.CustomerDiscount;
import ph.txtdis.dto.Discount;
import ph.txtdis.dto.Invoice;
import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.dto.OrderDetail;
import ph.txtdis.dto.Picking;
import ph.txtdis.dto.Price;
import ph.txtdis.dto.PricingType;
import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.dto.Receiving;
import ph.txtdis.dto.Remittance;
import ph.txtdis.dto.RemittancePayment;
import ph.txtdis.dto.Route;
import ph.txtdis.dto.Routing;
import ph.txtdis.dto.SoldDetail;
import ph.txtdis.dto.User;
import ph.txtdis.dto.VolumeDiscount;
import ph.txtdis.type.CustomerType;
import ph.txtdis.type.ItemTier;
import ph.txtdis.type.ItemType;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.ReceiptReferenceType;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VisitFrequency;
import ph.txtdis.type.VolumeDiscountType;

public class TestData {

	static final String BARE = "BARE";
	static final String SUPERMARKET = "SUPERMARKET";

	private Receiving getReceivingOfBareCustomer() {
		Receiving t = new Receiving();
		t.setId(1L);
		t.setReference(ReceiptReferenceType.SO);
		t.setReferenceId(1L);
		t.setCustomer(getCustomerOfBareType());
		t.setOrderDate(getDateOfNow());
		return t;
	}

	Account getAccountOfOgieNow() {
		Account a = new Account();
		a.setSeller(getUserOgie().getUsername());
		a.setStartDate(getDateOfNow());
		return a;
	}

	Account getAccountOfPhillip20150101() {
		Account a = new Account();
		a.setSeller(getUserPhillip().getUsername());
		a.setStartDate(getDateOf20150101());
		return a;
	}

	AbstractBookedOrder<Long> getBookedOrder() {
		return new AbstractBookedOrder<Long>() {
		};
	}

	Booking getBookingOfBareCustomer() {
		Booking t = new Booking();
		t.setId(1L);
		t.setCustomer(getCustomerOfBareType());
		t.setOrderDate(getDateOfNow());
		t.setDetails(getDetailsOfPineSliceByPieceBy10ByGood());
		return t;
	}

	Booking getBookingOfBareCustomerWithPerPieceAndPerCaseAndVolumeDiscountedItems() {
		Booking t = new Booking();
		t.setId(1L);
		t.setCustomer(getCustomerOfBareType());
		t.setOrderDate(getDateOfNow());
		t.setDetails(getDetailsOfPineSliceByByCaseOf10AndTjHotdogByPieceOf20());
		return t;
	}

	Booking getBookingOfCustomerWithCreditAndDiscount() {
		Booking t = new Booking();
		t.setId(2L);
		t.setCustomer(getCustomerWithCreditAndDiscount());
		t.setOrderDate(getDateOfNow());
		t.setDetails(getDetailsOfPineSliceByPieceBy20ByGood());
		t.setDiscounts(Arrays.asList(getDiscountOfLevel1Worth10Percent()));
		return t;
	}

	Channel getChannelOfBare() {
		Channel c = new Channel();
		c.setName(BARE);
		return c;
	}

	Channel getChannelOfSupermarket() {
		Channel c = new Channel();
		c.setName(SUPERMARKET);
		return c;
	}

	List<CreditDetail> getCreditDetailListOfOldNewAndFuture() {
		return Arrays.asList(getCreditDetailOf7Days0Grace10kLimitOldDate(),
				getCreditDetailOf30Days7Grace100kLimitNowDate(),
				getCreditDetailOf60Days15Grace1MLimitMonthFromNowDate());
	}

	CreditDetail getCreditDetailOf30Days7Grace100kLimitNowDate() {
		CreditDetail cd = new CreditDetail();
		cd.setTermInDays(30);
		cd.setGracePeriodInDays(7);
		cd.setCreditLimit(new BigDecimal(100_000));
		cd.setStartDate(getDateOfNow());
		return cd;
	}

	CreditDetail getCreditDetailOf60Days15Grace1MLimitMonthFromNowDate() {
		CreditDetail cd = new CreditDetail();
		cd.setTermInDays(60);
		cd.setGracePeriodInDays(15);
		cd.setCreditLimit(new BigDecimal(1_000_000));
		cd.setStartDate(getDateOfAMonthFromNow());
		return cd;
	}

	CreditDetail getCreditDetailOf7Days0Grace10kLimitOldDate() {
		CreditDetail cd = new CreditDetail();
		cd.setTermInDays(7);
		cd.setGracePeriodInDays(0);
		cd.setCreditLimit(new BigDecimal(10_000));
		cd.setStartDate(getDateOf20150101());
		return cd;
	}

	List<CustomerDiscount> getCustomerDiscountListOf0p10By0p05ForAllSince20150101() {
		return Arrays.asList(getCustomerDiscountOfLevel1By0p10ForAllSince20150101(),
				getCustomerDiscountOfLevel2By0p05ForAllSince20150101());
	}

	List<CustomerDiscount> getCustomerDiscountListOf0p30By0p15ForAllAMonthFromNow() {
		return Arrays.asList(getCustomerDiscountOfLevel1By0p30ForAllAMonthFromNow(),
				getCustomerDiscountOfLevel2By0p15ForAllAMonthFromNow());
	}

	List<CustomerDiscount> getCustomerDiscountListOf10by5ForRmFamilySince20150101() {
		return Arrays.asList(getCustomerDiscountOfLevel1By10p00ForRmFamilySince20150101(),
				getCustomerDiscountOfLevel2By5p00ForRmFamilySince20150101());
	}

	List<CustomerDiscount> getCustomerDiscountListOf1by0p50ForTjFamilySince20150101() {
		return Arrays.asList(getCustomerDiscountOfLevel1By1ForTjFamilySince20150101(),
				getCustomerDiscountOfLevel2By0p50ForTjFamilySince20150101());
	}

	List<CustomerDiscount> getCustomerDiscountListOf30by15ForAllAMonthFromNow() {
		return Arrays.asList(getCustomerDiscountOfLevel1By30p00ForDmFamilyAMonthFromNow(),
				getCustomerDiscountOfLevel2By15ForDmFamilyAMonthFromNow());
	}

	List<CustomerDiscount> getCustomerDiscountListOf3by1p5ForPineFamilyAMonthFromNow() {
		return Arrays.asList(getCustomerDiscountOfLevel1By3p00ForPineFamilyAMonthFromNow(),
				getCustomerDiscountOfLevel2By1p50ForPineFamilyAMonthFromNow());
	}

	List<CustomerDiscount> getCustomerDiscountListOfOldAndNew() {
		List<CustomerDiscount> list = new ArrayList<>(getCustomerDiscountListOf0p10By0p05ForAllSince20150101());
		list.addAll(getCustomerDiscountListOf1by0p50ForTjFamilySince20150101());
		list.addAll(getCustomerDiscountListOf10by5ForRmFamilySince20150101());
		list.addAll(getCustomerDiscountListOf0p30By0p15ForAllAMonthFromNow());
		list.addAll(getCustomerDiscountListOf3by1p5ForPineFamilyAMonthFromNow());
		list.addAll(getCustomerDiscountListOf30by15ForAllAMonthFromNow());
		return list;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By0p10ForAllSince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("0.10"));
		cd.setStartDate(getDateOf20150101());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By0p30ForAllAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("0.30"));
		cd.setStartDate(getDateOfAMonthFromNow());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By10p00ForRmFamilySince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("10.00"));
		cd.setStartDate(getDateOf20150101());
		cd.setFamilyLimit(getFamilyBizUnitRm());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By1ForTjFamilySince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("1.00"));
		cd.setStartDate(getDateOf20150101());
		cd.setFamilyLimit(getFamilyProdLineTjHotdog());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By30p00ForDmFamilyAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("30.00"));
		cd.setStartDate(getDateOfAMonthFromNow());
		cd.setFamilyLimit(getFamilyPrincipalDelMonte());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel1By3p00ForPineFamilyAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(1);
		cd.setPercent(new BigDecimal("3.00"));
		cd.setStartDate(getDateOfAMonthFromNow());
		cd.setFamilyLimit(getFamilyCategoryPine());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By0p05ForAllSince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("0.05"));
		cd.setStartDate(getDateOf20150101());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By0p15ForAllAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("0.15"));
		cd.setStartDate(getDateOfAMonthFromNow());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By0p50ForTjFamilySince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("0.50"));
		cd.setStartDate(getDateOf20150101());
		cd.setFamilyLimit(getFamilyProdLineTjHotdog());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By15ForDmFamilyAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("15.00"));
		cd.setStartDate(getDateOfAMonthFromNow());
		cd.setFamilyLimit(getFamilyPrincipalDelMonte());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By1p50ForPineFamilyAMonthFromNow() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("1.50"));
		cd.setStartDate(getDateOfAMonthFromNow());
		cd.setFamilyLimit(getFamilyCategoryPine());
		return cd;
	}

	CustomerDiscount getCustomerDiscountOfLevel2By5p00ForRmFamilySince20150101() {
		CustomerDiscount cd = new CustomerDiscount();
		cd.setLevel(2);
		cd.setPercent(new BigDecimal("5.00"));
		cd.setStartDate(getDateOf20150101());
		cd.setFamilyLimit(getFamilyBizUnitRm());
		return cd;
	}

	Customer getCustomerOfBareType() {
		Customer t = new Customer();
		t.setId(1L);
		t.setName(BARE);
		t.setType(CustomerType.OUTLET);
		t.setChannel(getChannelOfBare());
		t.setStreet("BARE ST.");
		t.setVisitFrequency(VisitFrequency.F2);
		t.setPrimaryPricingType(getPricingTypeDealer());
		t.setRouteHistory(Arrays.asList(getRoutingOfS41Since20150101()));
		return t;
	}

	Customer getCustomerOfCashierType() {
		Customer t = new Customer();
		t.setId(4L);
		t.setName("MAGNUM STA. MARIA");
		t.setType(CustomerType.CASHIER);
		t.setStreet("BAGBAGUIN ST.");
		return t;
	}

	Customer getCustomerOfSupermarketType() {
		Customer t = new Customer();
		t.setId(3L);
		t.setName(SUPERMARKET);
		t.setType(CustomerType.OUTLET);
		t.setChannel(getChannelOfSupermarket());
		t.setStreet("SUPERMARKET ROAD");
		t.setVisitFrequency(VisitFrequency.F4);
		t.setPrimaryPricingType(getPricingTypeSupermarket());
		t.setAlternatePricingType(getPricingTypeDealer());
		t.setCreditDetails(getCreditDetailListOfOldNewAndFuture());
		t.setDiscounts(getCustomerDiscountListOfOldAndNew());
		return t;
	}

	Customer getCustomerWithCreditAndDiscount() {
		Customer t = new Customer();
		t.setId(2L);
		t.setName("W/ CREDIT & DISCOUNT");
		t.setType(CustomerType.OUTLET);
		t.setStreet("CREDIT & DISCOUNT ROAD");
		t.setVisitFrequency(VisitFrequency.F2);
		t.setPrimaryPricingType(getPricingTypeDealer());
		t.setCreditDetails(getCreditDetailListOfOldNewAndFuture());
		t.setDiscounts(getCustomerDiscountListOfOldAndNew());
		t.setRouteHistory(Arrays.asList(getRoutingOfS42Since20150101()));
		return t;
	}

	LocalDate getDateOf20150101() {
		return LocalDate.of(2015, 1, 1);
	}

	LocalDate getDateOfAMonthFromNow() {
		return getDateOfNow().plusMonths(1);
	}

	LocalDate getDateOfNow() {
		return LocalDate.now();
	}

	SoldDetail getDetailOfPineSliceByCaseOf10AndGood() {
		SoldDetail sd = new SoldDetail();
		sd.setItem(getItemPineSliceFlat());
		sd.setUom(UomType.CS);
		sd.setQty(BigDecimal.TEN);
		sd.setQuality(QualityType.GOOD);
		sd.setPriceValue(getUnitPriceOf23p48().multiply(getQtyPerUomOfCaseType().getQty()));
		return sd;
	}

	SoldDetail getDetailOfPineSliceByPieceBy10ByGood() {
		SoldDetail sd = new SoldDetail();
		sd.setItem(getItemPineSliceFlat());
		sd.setUom(UomType.PC);
		sd.setQty(BigDecimal.TEN);
		sd.setQuality(QualityType.GOOD);
		sd.setPriceValue(getUnitPriceOf23p48());
		return sd;
	}

	SoldDetail getDetailOfPineSliceByPieceOf20AndGood() {
		SoldDetail sd = new SoldDetail();
		sd.setItem(getItemPineSliceFlat());
		sd.setUom(UomType.PC);
		sd.setQty(new BigDecimal("20"));
		sd.setQuality(QualityType.GOOD);
		sd.setPriceValue(getUnitPriceOf23p48());
		return sd;
	}

	SoldDetail getDetailOfTjReg1kPeBagByPieceOf20AndGood() {
		SoldDetail sd = new SoldDetail();
		sd.setItem(getItemTjReg1kPeBag());
		sd.setUom(UomType.PC);
		sd.setQty(new BigDecimal("20"));
		sd.setQuality(QualityType.GOOD);
		sd.setPriceValue(new BigDecimal("100.00"));
		return sd;
	}

	List<SoldDetail> getDetailsOfPineSliceByByCaseOf10AndTjHotdogByPieceOf20() {
		return Arrays.asList(getDetailOfPineSliceByCaseOf10AndGood(), getDetailOfTjReg1kPeBagByPieceOf20AndGood());
	}

	List<SoldDetail> getDetailsOfPineSliceByPieceBy10ByGood() {
		return Arrays.asList(getDetailOfPineSliceByPieceBy10ByGood());
	}

	List<SoldDetail> getDetailsOfPineSliceByPieceBy20ByGood() {
		return Arrays.asList(getDetailOfPineSliceByPieceOf20AndGood());
	}

	List<Discount> getDiscountListOf10() {
		return Arrays.asList(getEntityDiscountLevel1Of10());
	}

	List<Discount> getDiscountListOf10by5() {
		return Arrays.asList(getEntityDiscountLevel1Of10(), getEntityDiscountLevel2Of5());
	}

	Discount getDiscountOfLevel1Worth10Percent() {
		Discount d = new Discount();
		d.setLevel(1);
		d.setPercent(new BigDecimal("10.00"));
		return d;
	}

	Discount getEntityDiscountLevel1Of0p1() {
		Discount d = new Discount();
		d.setLevel(1);
		d.setPercent(new BigDecimal("0.10"));
		return d;
	}

	Discount getEntityDiscountLevel1Of1() {
		Discount d = new Discount();
		d.setLevel(1);
		d.setPercent(new BigDecimal("1.00"));
		return d;
	}

	Discount getEntityDiscountLevel1Of10() {
		Discount d = new Discount();
		d.setLevel(1);
		d.setPercent(new BigDecimal("10.00"));
		return d;
	}

	Discount getEntityDiscountLevel1Of3() {
		Discount d = new Discount();
		d.setLevel(1);
		d.setPercent(new BigDecimal("3.00"));
		return d;
	}

	Discount getEntityDiscountLevel2Of0p05() {
		Discount d = new Discount();
		d.setLevel(2);
		d.setPercent(new BigDecimal("0.05"));
		return d;
	}

	Discount getEntityDiscountLevel2Of0p5() {
		Discount d = new Discount();
		d.setLevel(2);
		d.setPercent(new BigDecimal("0.50"));
		return d;
	}

	Discount getEntityDiscountLevel2Of1p5() {
		Discount d = new Discount();
		d.setLevel(2);
		d.setPercent(new BigDecimal("1.50"));
		return d;
	}

	Discount getEntityDiscountLevel2Of5() {
		Discount d = new Discount();
		d.setLevel(2);
		d.setPercent(new BigDecimal("5.00"));
		return d;
	}

	List<Discount> getEntityDiscountList0p1by0p05() {
		return Arrays.asList(getEntityDiscountLevel1Of0p1(), getEntityDiscountLevel2Of0p05());
	}

	List<Discount> getEntityDiscountList1by0p5() {
		return Arrays.asList(getEntityDiscountLevel1Of1(), getEntityDiscountLevel2Of0p5());
	}

	List<Discount> getEntityDiscountList3by1p5() {
		return Arrays.asList(getEntityDiscountLevel1Of3(), getEntityDiscountLevel2Of1p5());
	}

	ItemFamily getFamilyBizUnitRm() {
		ItemFamily f = new ItemFamily();
		f.setName("RM");
		f.setTier(ItemTier.BIZUNIT);
		return f;
	}

	ItemFamily getFamilyCategoryPine() {
		ItemFamily f = new ItemFamily();
		f.setName("SOLIDS");
		f.setTier(ItemTier.CATEGORY);
		return f;
	}

	ItemFamily getFamilyCategoryRefMeat() {
		ItemFamily f = new ItemFamily();
		f.setName("REF MEATS");
		f.setTier(ItemTier.CATEGORY);
		return f;
	}

	List<ItemFamily> getFamilyListPine() {
		return Arrays.asList(getFamilyCategoryPine(), getFamilyPrincipalDelMonte());
	}

	List<ItemFamily> getFamilyListTj() {
		return Arrays.asList(getFamilyProdLineTjHotdog(), getFamilyCategoryRefMeat(), getFamilyBizUnitRm(),
				getFamilyPrincipalSmis());
	}

	ItemFamily getFamilyPrincipalDelMonte() {
		ItemFamily f = new ItemFamily();
		f.setName("DEL MONTE");
		f.setTier(ItemTier.PRINCIPAL);
		return f;
	}

	ItemFamily getFamilyPrincipalSmis() {
		ItemFamily f = new ItemFamily();
		f.setName("SMIS");
		f.setTier(ItemTier.PRINCIPAL);
		return f;
	}

	ItemFamily getFamilyProdLineTjHotdog() {
		ItemFamily f = new ItemFamily();
		f.setName("TJ HOTDOGS");
		f.setTier(ItemTier.PRODLINE);
		return f;
	}

	Invoice getInvoiceOfCustomerWithCreditAndDiscount() {
		Invoice t = new Invoice();
		t.setOrderDate(getDateOfNow());
		t.setId(1L);
		t.setNbrId(1L);
		t.setBooking(getBookingOfCustomerWithCreditAndDiscount());
		t.setCustomer(getBookingOfCustomerWithCreditAndDiscount().getCustomer());
		t.setDiscounts(Arrays.asList(getDiscountOfLevel1Worth10Percent()));
		t.setDetails(getDetailsOfPineSliceByPieceBy20ByGood());
		return t;
	}

	Item getItemPineSlice15() {
		Item t = new Item();
		t.setName("PINE SLCE 1.5 432G");
		t.setDescription("DEL MONTE SLICED PINEAPPLE 1 1/2 432G X 24");
		t.setType(ItemType.PURCHASED);
		t.setId(2L);
		t.setFamily(getFamilyCategoryPine());
		t.setVendorId("1596");
		t.setQtyPerUomList(getQtyPerUomListOfPieceAndCase());
		t.setPriceList(getPriceListOfPurchaseAndDealer());
		t.setVolumeDiscounts(
				getVolumeDiscountListOfTierTypeWorth10At10And20At20CasesSince20150101And30At30AMonthFromNow());
		return t;
	}

	Item getItemPineSliceFlat() {
		Item t = new Item();
		t.setName("PINE SLCE FLT 227G");
		t.setDescription("DEL MONTE SLICED PINEAPPLE FLAT 227G X 24");
		t.setType(ItemType.PURCHASED);
		t.setId(1L);
		t.setFamily(getFamilyCategoryPine());
		t.setVendorId("364");
		t.setQtyPerUomList(getQtyPerUomListOfPieceAndCase());
		t.setPriceList(getPriceListOfOldPurchaseAndNewPurchaseAndOldDealer());
		t.setVolumeDiscounts(
				getVolumeDiscountListOfSetTypeWorth1PerCaseAnd2ForSupermktSince20150101AndNoneAMonthFromNow());
		return t;
	}

	Item getItemTjReg1kPeBag() {
		Item t = new Item();
		t.setName("TJ REG 1KG");
		t.setDescription("TJ CLASSIC 1KG - PE BAG");
		t.setType(ItemType.PURCHASED);
		t.setId(4L);
		t.setFamily(getFamilyProdLineTjHotdog());
		t.setVendorId("5011200230029");
		t.setQtyPerUomList(Arrays.asList(getQtyPerUomOfPieceType()));
		t.setPriceList(getPriceListOfPurchaseAndDealer());
		return t;
	}

	Item getItemTjReg1kVac() {
		Item t = new Item();
		t.setName("TJ REG 1KG-V");
		t.setDescription("TJ CLASSIC 1KG - VACUUM");
		t.setType(ItemType.PURCHASED);
		t.setId(3L);
		t.setFamily(getFamilyProdLineTjHotdog());
		t.setVendorId("5011200230030");
		t.setQtyPerUomList(getQtyPerUomListOfPiece());
		t.setPriceList(getPriceListOfPurchaseAndDealerAndSupermarketByNow());
		return t;
	}

	Picking getPicking() {
		Picking t = new Picking();
		t.setId(1L);
		t.setPickDate(getDateOfNow());
		return t;
	}

	List<Price> getPriceListOfOldPurchaseAndNewPurchaseAndOldDealer() {
		return Arrays.asList(getPriceOfPurchaseTypeWorth20p70By20150101(), getPriceOfPurchaseTypeWorth21p70ByNow(),
				getPriceOfDealerTypeWorth23p48By20150101());
	}

	List<Price> getPriceListOfPurchaseAndDealer() {
		return Arrays.asList(getPriceOfDealerTypeWorth42p48By20150101(), getPriceOfPurchaseTypeWorth37p66By20150101());
	}

	List<Price> getPriceListOfPurchaseAndDealerAndSupermarketByNow() {
		return Arrays.asList(getPriceOfPurchaseTypeWorth90ByNow(), getPriceOfDealerTypeWorth100ByNow(),
				getPriceOfSupermarketTypeWorth110ByNow());
	}

	Price getPriceOfDealerTypeWorth100ByNow() {
		Price p = new Price();
		p.setType(getPricingTypeDealer());
		p.setPriceValue(new BigDecimal("100.00"));
		p.setStartDate(getDateOfNow());
		return p;
	}

	Price getPriceOfDealerTypeWorth23p48By20150101() {
		Price p = new Price();
		p.setType(getPricingTypeDealer());
		p.setPriceValue(new BigDecimal("23.48"));
		p.setStartDate(getDateOf20150101());
		return p;
	}

	Price getPriceOfDealerTypeWorth42p48By20150101() {
		Price p = new Price();
		p.setType(getPricingTypeDealer());
		p.setPriceValue(new BigDecimal("42.48"));
		p.setStartDate(getDateOf20150101());
		return p;
	}

	Price getPriceOfPurchaseTypeWorth20p70By20150101() {
		Price p = new Price();
		p.setType(getPricingTypePurchase());
		p.setPriceValue(new BigDecimal("20.70"));
		p.setStartDate(getDateOf20150101());
		return p;
	}

	Price getPriceOfPurchaseTypeWorth21p70ByNow() {
		Price p = new Price();
		p.setType(getPricingTypePurchase());
		p.setPriceValue(new BigDecimal("21.70"));
		p.setStartDate(getDateOfNow());
		return p;
	}

	Price getPriceOfPurchaseTypeWorth37p66By20150101() {
		Price p = new Price();
		p.setType(getPricingTypePurchase());
		p.setPriceValue(new BigDecimal("37.66"));
		p.setStartDate(getDateOf20150101());
		return p;
	}

	Price getPriceOfPurchaseTypeWorth90ByNow() {
		Price p = new Price();
		p.setType(getPricingTypePurchase());
		p.setPriceValue(new BigDecimal("90.00"));
		p.setStartDate(getDateOfNow());
		return p;
	}

	Price getPriceOfSupermarketTypeWorth110ByNow() {
		Price p = new Price();
		p.setType(getPricingTypeSupermarket());
		p.setPriceValue(new BigDecimal("110.00"));
		p.setStartDate(getDateOfNow());
		return p;
	}

	PricingType getPricingTypeDealer() {
		PricingType pt = new PricingType();
		pt.setName("DEALER");
		return pt;
	}

	PricingType getPricingTypePurchase() {
		PricingType pt = new PricingType();
		pt.setName("PURCHASE");
		return pt;
	}

	PricingType getPricingTypeSupermarket() {
		PricingType pt = new PricingType();
		pt.setName(SUPERMARKET);
		return pt;
	}

	List<QtyPerUom> getQtyPerUomListOfPiece() {
		return Arrays.asList(getQtyPerUomOfPieceType());
	}

	List<QtyPerUom> getQtyPerUomListOfPieceAndCase() {
		List<QtyPerUom> list = new ArrayList<QtyPerUom>(getQtyPerUomListOfPiece());
		list.add(getQtyPerUomOfCaseType());
		return list;
	}

	QtyPerUom getQtyPerUomOfCaseType() {
		QtyPerUom qpu = new QtyPerUom();
		qpu.setUom(UomType.CS);
		qpu.setQty(new BigDecimal("24.0000"));
		return qpu;
	}

	QtyPerUom getQtyPerUomOfPieceType() {
		QtyPerUom qpu = new QtyPerUom();
		qpu.setUom(UomType.PC);
		qpu.setQty(BigDecimal.ONE);
		return qpu;
	}

	Receiving getReceivingOfBareCustomerWith10PiecesPineSlice() {
		Receiving t = getReceivingOfBareCustomer();
		t.setDetails(getReturnsOfPineSliceByPieceBy10ByGood());
		return t;
	}

	Receiving getReceivingOfBareCustomerWith10PiecesTjReg1kPeBag() {
		Receiving t = getReceivingOfBareCustomer();
		t.setDetails(getReturnsOfTjReg1kPeBagByPieceBy10ByGood());
		return t;
	}

	Receiving getReceivingOfBareCustomerWith20PiecesTjReg1kPeBag() {
		Receiving t = getReceivingOfBareCustomer();
		t.setDetails(getReturnsOfTjReg1kPeBagByPieceBy20ByGood());
		return t;
	}

	Receiving getReceivingOfBareCustomerWithOneCasePineSlice() {
		Receiving t = getReceivingOfBareCustomer();
		t.setDetails(getReturnsOfPineSliceByCaseBy1ByGood());
		return t;
	}

	Remittance getRemittance() {
		Remittance r = new Remittance();
		r.setRemitDate(getDateOfNow());
		r.setBank(getCustomerOfCashierType());
		r.setValue(new BigDecimal(469.60));
		r.setCollector(getUserOgie());
		return r;
	}

	List<RemittancePayment> getRemittancePaymentListOf100by369p60DatedNow() {
		RemittancePayment t = new RemittancePayment();
		t.setRemitId(1L);
		t.setRemitDate(getDateOfNow());
		t.setValue(new BigDecimal("100.00"));
		List<RemittancePayment> list = new ArrayList<>(Arrays.asList(t));
		list.addAll(getRemittancePaymentListOf369p60DatedNow());
		return list;
	}

	List<RemittancePayment> getRemittancePaymentListOf369p60DatedNow() {
		RemittancePayment t = new RemittancePayment();
		t.setRemitId(2L);
		t.setRemitDate(getDateOfNow());
		t.setValue(new BigDecimal("369.60"));
		return Arrays.asList(t);
	}

	List<OrderDetail> getReturnsOfPineSliceByCaseBy1ByGood() {
		OrderDetail od = new OrderDetail();
		od.setItem(getItemPineSliceFlat());
		od.setUom(UomType.CS);
		od.setQty(BigDecimal.ONE);
		od.setQuality(QualityType.GOOD);
		return Arrays.asList(od);
	}

	List<OrderDetail> getReturnsOfPineSliceByCaseBy20ByGood() {
		OrderDetail od = new OrderDetail();
		od.setItem(getItemPineSliceFlat());
		od.setUom(UomType.CS);
		od.setQty(new BigDecimal("20"));
		od.setQuality(QualityType.GOOD);
		return Arrays.asList(od);
	}

	List<OrderDetail> getReturnsOfPineSliceByPieceBy10ByGood() {
		OrderDetail od = new OrderDetail();
		od.setItem(getItemPineSliceFlat());
		od.setUom(UomType.PC);
		od.setQty(BigDecimal.TEN);
		od.setQuality(QualityType.GOOD);
		return Arrays.asList(od);
	}

	List<OrderDetail> getReturnsOfTjReg1kPeBagByPieceBy10ByGood() {
		OrderDetail od = new OrderDetail();
		od.setItem(getItemTjReg1kPeBag());
		od.setUom(UomType.PC);
		od.setQty(BigDecimal.TEN);
		od.setQuality(QualityType.GOOD);
		return Arrays.asList(od);
	}

	List<OrderDetail> getReturnsOfTjReg1kPeBagByPieceBy20ByGood() {
		OrderDetail od = new OrderDetail();
		od.setItem(getItemTjReg1kPeBag());
		od.setUom(UomType.PC);
		od.setQty(new BigDecimal("20"));
		od.setQuality(QualityType.GOOD);
		return Arrays.asList(od);
	}

	Route getRouteOfS41() {
		Route s41 = new Route();
		s41.setName("S41");
		s41.setSellerHistory(Arrays.asList(getAccountOfPhillip20150101(), getAccountOfOgieNow()));
		return s41;
	}

	Route getRouteOfS42() {
		Route route = new Route();
		route.setName("S42");
		route.setSellerHistory(Arrays.asList(getAccountOfPhillip20150101()));
		return route;
	}

	Routing getRoutingOfS41Since20150101() {
		Routing r = new Routing();
		r.setRoute(getRouteOfS41());
		r.setStartDate(getDateOf20150101());
		return r;
	}

	Routing getRoutingOfS42Since20150101() {
		Routing r = new Routing();
		r.setRoute(getRouteOfS42());
		r.setStartDate(getDateOf20150101());
		return r;
	}

	ItemTree getTreePineByDelMonte() {
		ItemTree it = new ItemTree();
		it.setFamily(getFamilyCategoryPine());
		it.setParent(getFamilyPrincipalDelMonte());
		return it;
	}

	ItemTree getTreeRefMeatByRm() {
		ItemTree it = new ItemTree();
		it.setFamily(getFamilyCategoryRefMeat());
		it.setParent(getFamilyBizUnitRm());
		return it;
	}

	ItemTree getTreeRmBySmis() {
		ItemTree it = new ItemTree();
		it.setFamily(getFamilyBizUnitRm());
		it.setParent(getFamilyPrincipalSmis());
		return it;
	}

	ItemTree getTreeTjByRefMeat() {
		ItemTree it = new ItemTree();
		it.setFamily(getFamilyProdLineTjHotdog());
		it.setParent(getFamilyCategoryRefMeat());
		return it;
	}

	BigDecimal getUnitPriceOf23p48() {
		return new BigDecimal("23.48");
	}

	User getUserOgie() {
		User u = new User();
		u.setUsername("OGIE");
		return u;
	}

	User getUserPhillip() {
		User u = new User();
		u.setUsername("PHILLIP");
		return u;
	}

	BigDecimal getVatDivisorOf1p12() {
		return new BigDecimal("1.12");
	}

	List<VolumeDiscount> getVolumeDiscountListOfSetTypeWorth1PerCaseAnd2ForSupermktSince20150101AndNoneAMonthFromNow() {
		return Arrays.asList(getVolumeDiscountOfSetTypeWorth1PesoPerCaseSince20150101(),
				getVolumeDiscountOfSetTypeWorth2PesoPerCaseSince20150101ForSupermarket(),
				getVolumeDiscountOfSetTypeDiscontinuedAMonthFromNow());
	}

	List<VolumeDiscount> getVolumeDiscountListOfTierTypeWorth10At10And20At20CasesSince20150101And30At30AMonthFromNow() {
		return Arrays.asList(getVolumeDiscountOfTierTypeWorth10PesosUponReaching10CasesSince20150101(),
				getVolumeDiscountOfTierTypeWorth20PesosUponReaching20CasesSince20150101(),
				getVolumeDiscountOfTierTypeWorth40PesosUponReaching30CasesAMonthFromNow(),
				getVolumeDiscountOfTierTypeWorth30PesosUponReaching20CasesSince20150101ForSupermarkets());
	}

	VolumeDiscount getVolumeDiscountOfSetTypeDiscontinuedAMonthFromNow() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.SET);
		vd.setUom(UomType.PC);
		vd.setCutOff(1);
		vd.setDiscount(BigDecimal.ZERO);
		vd.setStartDate(getDateOfAMonthFromNow());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfSetTypeWorth1PesoPerCaseSince20150101() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.SET);
		vd.setUom(UomType.CS);
		vd.setCutOff(1);
		vd.setDiscount(new BigDecimal("1.00"));
		vd.setStartDate(getDateOf20150101());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfSetTypeWorth2PesoPerCaseSince20150101ForSupermarket() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.SET);
		vd.setUom(UomType.CS);
		vd.setCutOff(1);
		vd.setDiscount(new BigDecimal("2.00"));
		vd.setStartDate(getDateOf20150101());
		vd.setChannelLimit(getChannelOfSupermarket());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfTierTypeWorth10PesosUponReaching10CasesSince20150101() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.TIER);
		vd.setUom(UomType.CS);
		vd.setCutOff(10);
		vd.setDiscount(new BigDecimal("10.00"));
		vd.setStartDate(getDateOf20150101());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfTierTypeWorth20PesosUponReaching20CasesSince20150101() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.TIER);
		vd.setUom(UomType.CS);
		vd.setCutOff(20);
		vd.setDiscount(new BigDecimal("20.00"));
		vd.setStartDate(getDateOf20150101());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfTierTypeWorth30PesosUponReaching20CasesSince20150101ForSupermarkets() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.TIER);
		vd.setUom(UomType.CS);
		vd.setCutOff(20);
		vd.setDiscount(new BigDecimal("30.00"));
		vd.setStartDate(getDateOf20150101());
		vd.setChannelLimit(getChannelOfSupermarket());
		return vd;
	}

	VolumeDiscount getVolumeDiscountOfTierTypeWorth40PesosUponReaching30CasesAMonthFromNow() {
		VolumeDiscount vd = new VolumeDiscount();
		vd.setType(VolumeDiscountType.TIER);
		vd.setUom(UomType.CS);
		vd.setCutOff(30);
		vd.setDiscount(new BigDecimal("40.00"));
		vd.setStartDate(getDateOfAMonthFromNow());
		return vd;
	}
}
