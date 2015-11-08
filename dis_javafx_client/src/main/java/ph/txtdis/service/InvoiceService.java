package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.StringUtils.splitByCharacterType;

import static java.math.BigDecimal.ONE;

import static ph.txtdis.type.ModuleType.BOOKING;
import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.INVOICE;

import ph.txtdis.dto.Billable;
import ph.txtdis.dto.BillableDetail;
import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.PickList;
import ph.txtdis.exception.AlreadyReferencedBookingIdException;
import ph.txtdis.exception.DateInTheFutureException;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.exception.GapInSerialInvoiceIdException;
import ph.txtdis.exception.InvalidDateSequenceException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.NotPickedBookingIdException;
import ph.txtdis.exception.UnissuedInvoiceIdException;
import ph.txtdis.type.ModuleType;

@Service("invoiceService")
public class InvoiceService extends SoldService<Billable, Long> implements Reset {

	@Autowired
	private InvoiceBookletService bookletService;

	@Autowired
	private PickListService pickService;

	@Autowired
	private ReadOnlyService<Billable> readOnlyService;

	private String prefix, idNo, suffix;

	private ModuleType type;

	public InvoiceService() {
		reset();
	}

	public Billable findByOrderNo(String id) throws Exception {
		if (isABooking())
			return findBookingById(id);
		return findByOrderId(id);
	}

	@Override
	public String getAlternateName() {
		if (isAnInvoice())
			return "S/I";
		if (isABooking())
			return "S/O";
		return "D/R";
	}

	public BigDecimal getBalance() {
		try {
			BigDecimal d = get().getUnpaidValue();
			return d.compareTo(ONE) <= 0 ? null : d;
		} catch (Exception e) {
			return null;
		}
	}

	public Long getBookingId() {
		return get().getBookingId();
	}

	@Override
	public String getHeaderText() {
		if (isABooking())
			return "Sales Order";
		if (isAnInvoice())
			return super.getHeaderText();
		return "Delivery Report";
	}

	@Override
	public String getModule() {
		return "invoice";
	}

	@Override
	public String getModuleId() {
		return getAlternateName() + " " + getOrderNo();
	}

	public Long getNumId() {
		return Math.abs(get().getNumId());
	}

	@Override
	public String getOpenDialogHeading() {
		return "Open a(n) " + getHeaderText();
	}

	public String getOrderNo() {
		String s = get().getOrderNo();
		if (isAnInvoice())
			return s;
		if (isADeliveryReport())
			return s.replace("-", "");
		Long id = get().getBookingId();
		return id == null ? "" : id.toString();
	}

	public List<String> getPaymentList() {
		return get().getPayments();
	}

	public String getPrefix() {
		return get().getPrefix();
	}

	@Override
	public String getRemarks() {
		String s = get().getRemarks();
		return s == null ? "" : s;
	}

	@Override
	public Long getSpunId() {
		if (isNew())
			return null;
		return isABooking() ? getBookingId() : getId();
	}

	@Override
	public String getSpunModule() {
		if (isAnInvoice())
			return getModule();
		if (isADeliveryReport())
			return "deliveryReport";
		return "booking";
	}

	public String getSuffix() {
		return get().getSuffix();
	}

	public boolean isABooking() {
		return type == BOOKING;
	}

	public boolean isADeliveryReport() {
		return type == DELIVERY_REPORT;
	}

	public boolean isAnInvoice() {
		return type == INVOICE;
	}

	@Override
	public void reset() {
		super.reset();
		set(new Billable());
	}

	@Override
	public void setDetails(List<BillableDetail> details) {
		super.setDetails(details);
		computeUnpaid();
	}

	@Override
	public void setOrderDateUponValidation(LocalDate date) throws Exception {
		if (date != null) {
			if (date.isAfter(LocalDate.now()))
				throw new DateInTheFutureException();
			get().setOrderDate(date);
		}
	}

	@Override
	public void setRemarks(String s) {
		get().setRemarks(s);
	}

	public void setType(ModuleType t) {
		type = t;
	}

	@Override
	public void updatePerValidity(Boolean b) {
		// TODO Auto-generated method stub
	}

	public void updateUponBookingIdValidation(long id) throws Exception {
		if (id == 0)
			return;
		Billable b = findBookingById(String.valueOf(id));
		if (b == null)
			throw new NotFoundException("S/O No. " + id);
		if (b.getNumId() != 0L)
			throw new AlreadyReferencedBookingIdException(id, b);
		confirmBookingHasBeenPicked(id);
		set(b);
	}

	public void updateUponOrderNoValidation(String prefix, Long id, String suffix) throws Exception {
		checkforDuplicates(prefix, id, suffix);
		verifyIdIsPartOfAnIssuedBookletImmediatelyPrecedingItsLast(prefix, id, suffix);
		setIds(prefix, id, suffix);
	}

	private void checkforDuplicates(String prefix, Long id, String suffix) throws Exception {
		Billable i = readOnlyService.module(getModule())
				.getOne("/find?prefix=" + prefix + "&id=" + id + "&suffix=" + suffix);
		if (i != null)
			throw new DuplicateException(getModuleId() + id);
	}

	private void computeUnpaid() {
		if (getPaymentList() == null || getPaymentList().isEmpty())
			get().setUnpaidValue(getTotal());
	}

	private void confirmBookingHasBeenPicked(Long id) throws Exception {
		PickList p = pickService.getByBooking(id);
		if (p == null)
			throw new NotPickedBookingIdException(id);
		if (get().getOrderDate().isBefore(p.getPickDate()))
			throw new InvalidDateSequenceException(getAlternateName(), get().getOrderDate(), "Pick", p.getPickDate());
	}

	private Billable findBookingById(String id) throws Exception {
		Billable b = readOnlyService.module(getModule()).getOne("/booking?id=" + id);
		if (b == null)
			throw new NotFoundException("S/O No. " + id);
		return b;
	}

	private Billable findByOrderId(String id) throws Exception, NotFoundException {
		setIdsFromOrderNo(id);
		Billable b = readOnlyService.module(getModule())
				.getOne("/find?prefix=" + prefix + "&id=" + idNo + "&suffix=" + suffix);
		if (b == null)
			throw new NotFoundException(getModuleId() + id);
		return b;
	}

	private Long latestUsedIdInBooklet(InvoiceBooklet b) throws Exception {
		Billable i = readOnlyService.module(getModule()).getOne("/latest?prefix=" + b.getPrefix() + "&suffix="
				+ b.getSuffix() + "&start=" + b.getStartId() + "&end=" + b.getEndId());
		return i == null ? b.getStartId() - 1 : i.getNumId();
	}

	private void setIds(String prefix, Long id, String suffix) {
		setPrefix(prefix);
		setNbrId(id);
		setSuffix(suffix);
	}

	private void setIdsForDeliveryReport(String id) {
		setTextIds("", "-" + id, "");
	}

	private void setIdsForInvoice(String id) throws NotFoundException {
		String[] ids = StringUtils.split(id, "-");
		if (ids == null || ids.length == 0 || ids.length > 2)
			throw new NotFoundException(getModuleId() + id);
		if (ids.length == 1)
			setIdsWithoutPrefix(id, ids);
		else
			setIdsWithPrefix(id, ids);
	}

	private void setIdsFromOrderNo(String id) throws Exception {
		if (isAnInvoice())
			setIdsForInvoice(id);
		else
			setIdsForDeliveryReport(id);
	}

	private void setIdsWithNumbersOnly(String orderNo, String idNo) throws NotFoundException {
		if (!isNumeric(idNo.replace("-", "")))
			throw new NotFoundException(getModuleId() + orderNo);
		setTextIds("", idNo, "");
	}

	private void setIdsWithoutPrefix(String orderNo, String[] ids) throws NotFoundException {
		ids = StringUtils.splitByCharacterType(ids[0]);
		if (ids.length > 2)
			throw new NotFoundException(getModuleId() + orderNo);
		if (ids.length == 1)
			setIdsWithNumbersOnly(orderNo, ids[0]);
		else
			setTextIds("", ids[0], ids[1]);
	}

	private void setIdsWithoutSuffix(String orderNo, String code, String number) throws NotFoundException {
		if (!isNumeric(number))
			throw new NotFoundException(getModuleId() + orderNo);
		setTextIds(code, number, "");
	}

	private void setIdsWithPrefix(String orderNo, String[] ids) throws NotFoundException {
		String[] nos = splitByCharacterType(ids[1]);
		if (nos.length > 2)
			throw new NotFoundException(getModuleId() + orderNo);
		if (nos.length == 1)
			setIdsWithoutSuffix(orderNo, ids[0], nos[0]);
		else
			setTextIds(ids[0], nos[0], nos[1]);
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

	private void setTextIds(String... ids) {
		this.prefix = ids[0];
		this.idNo = ids[1];
		this.suffix = ids[2];
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
}
