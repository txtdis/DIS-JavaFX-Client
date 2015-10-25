package ph.txtdis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.NoArgsConstructor;
import ph.txtdis.dto.AbstractBookedOrder;
import ph.txtdis.dto.Booking;
import ph.txtdis.dto.OrderDetail;
import ph.txtdis.dto.PickList;
import ph.txtdis.dto.Receiving;
import ph.txtdis.dto.RemittancePayment;
import ph.txtdis.dto.SoldOrderDetail;
import ph.txtdis.exception.AlreadyReferencedBookingIdException;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.InvalidDateSequenceException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.NotPickedBookingIdException;
import ph.txtdis.type.UomType;
import ph.txtdis.util.Numeric;
import ph.txtdis.util.Temporal;

@NoArgsConstructor
public abstract class BookedService<T extends AbstractBookedOrder<PK>, PK> extends SoldService<T, PK>
		implements AlternateNamed
{

	@Autowired
	private BookingService bookingService;

	@Autowired
	private PickListService pickService;

	@Autowired
	private ReceivingService receivingService;

	BookedService(ReadOnlyService<T> readOnlyService, BookingService bookingService, PickListService pickService,
			ReceivingService receivingService) {
		this.readOnlyService = readOnlyService;
		this.bookingService = bookingService;
		this.pickService = pickService;
		this.receivingService = receivingService;
	}

	public BigDecimal getBalance() {
		return get().getUnpaidValue();
	}

	public Long getBookingId() {
		return get().getBooking() == null ? null : get().getBooking().getId();
	}

	public List<String> getPaymentList() {
		List<RemittancePayment> payments = getPayments();
		ArrayList<String> list = new ArrayList<>();
		if (payments == null || payments.isEmpty())
			return list;
		if (payments.size() > 1)
			list.add(getTotalPaymentText(payments));
		return getRemitIdAndDateAndPaymentTextList(payments, list);
	}

	public void resetBooking() {
		extract(new Booking());
	}

	@Override
	public void setDetails(List<SoldOrderDetail> details) {
		super.setDetails(details);
		get().setUnpaidValue(computeUnpaid());
	}

	@Override
	public void setOrderDateUponValidation(LocalDate date) throws Exception {
		if (date != null) {
			if (date.isAfter(LocalDate.now()))
				throw new DateInTheFutureException();
			get().setOrderDate(date);
		}
	}

	public void updateUponBookingIdValidation(long id) throws Exception {
		if (id != 0) {
			ensureBookingIdExists(id);
			verifyBookingHasNotBeenReferenced(id);
			confirmBookingHasBeenPicked(id);
			setAsBookedOrder(id);
			netReturnedItems(id);
		}
	}

	private boolean areUomsEqual(SoldOrderDetail booked, OrderDetail received) {
		return booked.getUom() == received.getUom();
	}

	private BigDecimal computeUnpaid() {
		return getPayments() == null ? getTotalValue() : getTotalValue().subtract(sumPayments(getPayments()));
	}

	private void confirmBookingHasBeenPicked(Long id) throws Exception {
		PickList p = pickService.getByBooking(id);
		if (p == null)
			throw new NotPickedBookingIdException(id);
		if (get().getOrderDate().isBefore(p.getPickDate()))
			throw new InvalidDateSequenceException(getAlternateName(), get().getOrderDate(), "Pick", p.getPickDate());
	}

	private SoldOrderDetail createDetail(SoldOrderDetail booked, OrderDetail returned, BigDecimal qtyInPieces)
			throws Exception {
		SoldOrderDetail sd = new SoldOrderDetail();
		sd.setItem(booked.getItem());
		sd.setUom(getUom(booked, returned));
		sd.setQty(getQtyPerUom(booked, returned, qtyInPieces));
		sd.setQuality(booked.getQuality());
		sd.setPriceValue(getPrice(booked, returned, qtyInPieces));
		return sd;
	}

	private String createRemitIdAndDateAndPaymentText(RemittancePayment p) {
		return "[R/S No. " + p.getRemitId() + " - " + Temporal.format(p.getRemitDate()) + "] "
				+ Numeric.formatCurrency(p.getValue());
	}

	private void ensureBookingIdExists(Long id) throws Exception {
		if (bookingService.find(id.toString()) == null)
			throw new NotFoundException("S/O No. " + id);
	}

	private void extract(Booking booking) {
		setBooking(booking);
		get().setCustomer(booking.getCustomer());
		get().setCredit(booking.getCredit());
		get().setRemarks(booking.getRemarks());
		get().setRoute(booking.getRoute());
		get().setDetails(booking.getDetails());
		get().setDiscounts(booking.getDiscounts());
	}

	private SoldOrderDetail getNettedDetail(SoldOrderDetail booked, OrderDetail returned) throws Exception {
		SoldOrderDetail detail = null;
		BigDecimal qtyInPieces = netQty(booked, returned);
		if (qtyInPieces.compareTo(BigDecimal.ZERO) > 0)
			detail = createDetail(booked, returned, qtyInPieces);
		return detail;
	}

	private BigDecimal getNormalPrice(SoldOrderDetail booked, OrderDetail received) throws Exception {
		if (areUomsEqual(booked, received))
			return booked.getPriceValue();
		return getUnitPrice();
	}

	private List<RemittancePayment> getPayments() {
		return get().getPayments();
	}

	private BigDecimal getPrice(SoldOrderDetail booked, OrderDetail received, BigDecimal qty) throws Exception {
		if (isVolumeDiscounted(booked.getItem()))
			return getVolumeDiscountedPrice(booked, received, qty);
		return getNormalPrice(booked, received);
	}

	private BigDecimal getQtyPerUom(SoldOrderDetail booked, OrderDetail received, BigDecimal qty) {
		return !areUomsEqual(booked, received) ? qty
				: qty.divide(getQtyPerUom(booked.getUom()), 8, RoundingMode.HALF_EVEN);
	}

	private List<String> getRemitIdAndDateAndPaymentTextList(List<RemittancePayment> r, List<String> list) {
		r.forEach(p -> list.add(createRemitIdAndDateAndPaymentText(p)));
		return list;
	}

	private String getTotalPaymentText(List<RemittancePayment> p) {
		return "[TOTAL] " + Numeric.formatCurrency(sumPayments(p));
	}

	private UomType getUom(SoldOrderDetail booked, OrderDetail received) {
		return areUomsEqual(booked, received) ? booked.getUom() : UomType.PC;
	}

	private BigDecimal getVolumeDiscountedPrice(SoldOrderDetail booked, OrderDetail received, BigDecimal qty)
			throws Exception {
		BigDecimal unitPrice = computeDiscountedPrice(qty);
		return areUomsEqual(booked, received) ? unitPrice.multiply(getQtyPerUom(booked.getUom())) : unitPrice;
	}

	private BigDecimal netQty(SoldOrderDetail booked, OrderDetail received) {
		BigDecimal bookedQty = booked.getQty().multiply(getQtyPerUom(booked.getUom()));
		BigDecimal receivedQty = received.getQty().multiply(getQtyPerUom(received.getUom()));
		return bookedQty.subtract(receivedQty);
	}

	private void netReturnedItems(long id) throws Exception {
		try {
			netReturnedItems(receivingService.findByBooking(id));
		} catch (Exception e) {
			if (!(e instanceof NotFoundException))
				throw e;
		}
	}

	private void netReturnedItems(Receiving receiving) throws Exception {
		Iterator<SoldOrderDetail> it = getDetails().iterator();
		List<SoldOrderDetail> list = new ArrayList<>(getDetails());
		int i = 0, j = 0;
		while (it.hasNext()) {
			for (OrderDetail rd : receiving.getDetails()) {
				SoldOrderDetail bd = getDetails().get(j++);
				if (rd.getItem().equals(bd.getItem())) {
					setItem(bd.getItem());
					setLatestPrice(getItem());
					SoldOrderDetail sd = getNettedDetail(bd, rd);
					if (sd == null)
						list.remove(i);
					else
						list.set(i++, sd);
					break;
				}
			}
			it.next();
		}
		setDetails(list);
	}

	private void setAsBookedOrder(Long id) throws Exception {
		extract(bookingService.find(id.toString()));
	}

	private void setBooking(Booking b) {
		get().setBooking(b);
	}

	private BigDecimal sumPayments(List<RemittancePayment> payments) {
		return payments == null ? BigDecimal.ZERO
				: payments.stream().map(t -> t.getValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	void verifyBookingHasNotBeenReferenced(Long id) throws Exception {
		T booked = readOnlyService.module(getModule()).getOne("/booking?id=" + id);
		if (booked != null)
			throw new AlreadyReferencedBookingIdException(id, booked);
	}
}
