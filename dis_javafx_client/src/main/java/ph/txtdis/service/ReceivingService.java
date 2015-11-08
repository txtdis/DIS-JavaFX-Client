package ph.txtdis.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import ph.txtdis.dto.Billable;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.type.UomType;

@Service
@NoArgsConstructor
public class ReceivingService extends SoldService<Billable, Long> {

	@Autowired
	private ReadOnlyService<Billable> readOnlyService;

	@Override
	public String getAlternateName() {
		return "R/R";
	}

	@Override
	public String getModule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemarks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void next() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void previous() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRemarks(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePerValidity(Boolean b) {
		// TODO Auto-generated method stub

	}

	private boolean areUomsEqual(BillableDetail booked, BillableDetail received) {
		return booked.getUom() == received.getUom();
	}

	private BigDecimal getNormalPrice(BillableDetail booked, BillableDetail received) throws Exception {
		if (areUomsEqual(booked, received))
			return booked.getPriceValue();
		return getUnitPrice();
	}

	private BigDecimal getPrice(BillableDetail booked, BillableDetail received, BigDecimal qty) throws Exception {
		if (isVolumeDiscounted(getItem()))
			return getVolumeDiscountedPrice(booked, received, qty);
		return getNormalPrice(booked, received);
	}

	private BigDecimal getQtyPerUom(BillableDetail booked, BillableDetail received, BigDecimal qty) {
		return !areUomsEqual(booked, received) ? qty
				: qty.divide(getQtyPerUom(booked.getUom()), 8, RoundingMode.HALF_EVEN);
	}

	private UomType getUom(BillableDetail booked, BillableDetail received) {
		return areUomsEqual(booked, received) ? booked.getUom() : UomType.PC;
	}

	private BigDecimal getVolumeDiscountedPrice(BillableDetail booked, BillableDetail received, BigDecimal qty)
			throws Exception {
		BigDecimal unitPrice = computeDiscountedPrice(qty);
		return areUomsEqual(booked, received) ? unitPrice.multiply(getQtyPerUom(booked.getUom())) : unitPrice;
	}

	private BigDecimal netQty(BillableDetail booked, BillableDetail received) {
		BigDecimal bookedQty = booked.getQty().multiply(getQtyPerUom(booked.getUom()));
		BigDecimal receivedQty = received.getQty().multiply(getQtyPerUom(received.getUom()));
		return bookedQty.subtract(receivedQty);
	}

	protected Billable findByBooking(Long id) throws Exception {
		Billable rr = readOnlyService.module("receiving").getOne("/booking?id=" + id);
		if (rr == null)
			throw new NotFoundException("R/R No. " + id);
		return rr;
	}
}
