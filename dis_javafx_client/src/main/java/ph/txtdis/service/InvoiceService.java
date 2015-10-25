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
public class InvoiceService extends BookedService<Invoice, Long> implements Reset {

	@Autowired
	private InvoiceBookletService bookletService;

	@Autowired
	private ReadOnlyService<Invoice> readOnlyService;

	private String prefix, idNo, suffix;

	public InvoiceService() {
		reset();
	}

	@Override
	public Invoice find(String id) throws Exception {
		return readOnlyService.module(getModule()).getOne("/" + id);
	}

	public Invoice findByInvoiceId(String id) throws Exception {
		setIds(id);
		Invoice e = readOnlyService.module(getModule())
				.getOne("/find?prefix=" + prefix + "&id=" + idNo + "&suffix=" + suffix);
		if (e == null)
			throw new NotFoundException(getModuleId() + id);
		return e;
	}

	@Override
	public String getAlternateName() {
		return "S/I";
	}

	@Override
	public String getModule() {
		return "invoice";
	}

	public Long getNbrId() {
		return get().getNumId();
	}

	public String getOrderNo() {
		return get().getOrderNo();
	}

	public String getPrefix() {
		return get().getPrefix();
	}

	public String getSuffix() {
		return get().getSuffix();
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
				.getOne("/find?prefix=" + prefix + "&id=" + id + "&suffix=" + suffix);
		if (i != null)
			throw new DuplicateException("ID No. " + id);
	}

	private Long latestUsedIdInBooklet(InvoiceBooklet b) throws Exception {
		Invoice i = readOnlyService.module("invoice").getOne("/latest?prefix=" + b.getPrefix() + "&suffix="
				+ b.getSuffix() + "&start=" + b.getStartId() + "&end=" + b.getEndId());
		return i == null ? b.getStartId() - 1 : i.getNumId();
	}

	private void setIds(String id) throws Exception {
		String[] ids = StringUtils.split(id, "-");
		if (ids == null || ids.length == 0 || ids.length > 2)
			throw new NotFoundException(getModuleId() + id);
		if (ids.length == 1)
			setIdsWithNoCodes(id, ids);
		else
			setIdsWithCode(id, ids);
	}

	private void setIds(String... ids) {
		this.prefix = ids[0];
		this.idNo = ids[1];
		this.suffix = ids[2];
	}

	private void setIds(String prefix, Long id, String suffix) {
		setPrefix(prefix);
		setNbrId(id);
		setSuffix(suffix);
	}

	private void setIdsWithCode(String id, String[] ids) throws NotFoundException {
		String[] nos = StringUtils.splitByCharacterType(ids[1]);
		if (nos.length > 2)
			throw new NotFoundException(getModuleId() + id);
		if (nos.length == 1)
			setIdsWithoutSeries(id, ids[0], nos[0]);
		else
			setIds(ids[0], nos[0], nos[1]);
	}

	private void setIdsWithNoCodes(String id, String[] ids) throws NotFoundException {
		ids = StringUtils.splitByCharacterType(ids[0]);
		if (ids.length > 2)
			throw new NotFoundException(getModuleId() + id);
		if (ids.length == 1)
			setIdsWithNumbersOnly(id, ids[0]);
		else
			setIds("", ids[0], ids[1]);
	}

	private void setIdsWithNumbersOnly(String id, String number) throws NotFoundException {
		if (!StringUtils.isNumeric(number))
			throw new NotFoundException(getModuleId() + id);
		setIds("", number, "");
	}

	private void setIdsWithoutSeries(String id, String code, String number) throws NotFoundException {
		if (!StringUtils.isNumeric(number))
			throw new NotFoundException(getModuleId() + id);
		setIds(code, number, "");
	}

	private void setNbrId(Long id) {
		get().setNumId(id);
	}

	private void setPrefix(String prefix) {
		get().setPrefix(prefix);
	}

	private void setSuffix(String suffix) {
		get().setSuffix(suffix);
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
	}
}
