package ph.txtdis.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Invoice;
import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.GapInSerialInvoiceIdException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.UnissuedInvoiceIdException;

@Service("invoiceService")
public class InvoiceService extends BookedService<Invoice, String> implements Reset {

	@Autowired
	private InvoiceBookletService bookletService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private ReadOnlyService<Invoice> readOnlyService;

	private String prefix, idNo, suffix;

	public InvoiceService() {
		reset();
	}

	@Override
	public Invoice find(String id) throws Exception {
		setIds(id);
		Invoice e = readOnlyService.module(getModule())
				.getOne("/find?prefix=" + prefix + "&id=" + idNo + "&suffix=" + suffix);
		if (e == null)
			throw new NotFoundException("ID No. " + id);
		return e;
	}

	@Override
	public String getAlternateName() {
		return "S/I";
	}

	public Long getIdNo() {
		return get().getIdNo();
	}

	public String getIdPrefix() {
		return get().getIdPrefix();
	}

	public String getIdSuffix() {
		return get().getIdSuffix();
	}

	@Override
	public void reset() {
		set(new Invoice());
	}

	public void updateUponInvoiceIdValidation(String prefix, Long id, String suffix) throws Exception {
		checkforDuplicates(prefix, id, suffix);
		verifyIdIsPartOfAnIssuedBookletImmediatelyPrecedingItsLast(prefix, id, suffix);
		setIds(prefix, id, suffix);
	}

	private void checkforDuplicates(String prefix, Long id, String suffix) throws Exception {
		Invoice i = readOnlyService.module("invoice")
				.getOne("/find?prefix=" + prefix + "&id=" + idNo + "&suffix=" + suffix);
		if (i != null)
			throw new DuplicateException("ID No. " + id);
	}

	private Long latestUsedIdInBooklet(InvoiceBooklet booklet) throws Exception {
		Invoice i = readOnlyService.module("invoice")
				.getOne("/latest?start=" + booklet.getStartId() + "&end=" + booklet.getEndId());
		return i == null ? booklet.getStartId() - 1 : i.getIdNo();
	}

	private void setIdNo(Long id) {
		get().setIdNo(id);
	}

	private void setIdPrefix(String prefix) {
		get().setIdPrefix(prefix);
	}

	private void setIds(String id) {
		String[] ids = StringUtils.splitByCharacterType(id);
		if (ids == null || ids.length == 0 || ids.length > 3)
			throw new NotFoundException("ID No. " + id);
		if (ids.length == 1) {
			setIds("", ids[0], "");
		} else if (ids.length == 3) {
			setIds(ids[0], ids[1], ids[3]);
		} else if (StringUtils.isAlpha(ids[0])) {
			setIds(ids[0], ids[1], "");
		} else {
			setIds("", ids[0], ids[1]);
		}
	}

	private void setIds(String... ids) {
		this.prefix = ids[0];
		this.idNo = ids[1];
		this.suffix = ids[2];
	}

	private void setIds(String prefix, Long id, String suffix) {
		setIdPrefix(prefix);
		setIdNo(id);
		setIdSuffix(suffix);
	}

	private void setIdSuffix(String suffix) {
		get().setIdSuffix(suffix);
	}

	private void verifyIdIsPartOfAnIssuedBookletImmediatelyPrecedingItsLast(String prefix, Long id, String suffix)
			throws Exception {
		InvoiceBooklet booklet = bookletService.find(prefix, id, suffix);
		if (booklet == null)
			throw new UnissuedInvoiceIdException(prefix + id + suffix);
		Long nextIdInBooklet = latestUsedIdInBooklet(booklet) + 1;
		if (id != nextIdInBooklet)
			throw new GapInSerialInvoiceIdException(nextIdInBooklet);
	}

	@Override
	protected void verifyBookingHasNotBeenReferenced(Long id) throws Exception {
		super.verifyBookingHasNotBeenReferenced(id);
		deliveryService.verifyNoDeliveryReportReferencedBooking(id);
	}

	protected void verifyNoInvoiceReferencedBooking(Long id) throws Exception {
		super.verifyBookingHasNotBeenReferenced(id);
	}
}
