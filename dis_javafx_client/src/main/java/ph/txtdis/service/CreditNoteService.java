package ph.txtdis.service;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ZERO;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import ph.txtdis.dto.CreditNote;
import ph.txtdis.dto.CreditNoteDump;
import ph.txtdis.dto.CreditNotePayment;
import ph.txtdis.dto.Keyed;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Scope("prototype")
@Service("creditNoteService")
public class CreditNoteService implements Excel<CreditNote>, Iconed, Listed<CreditNote>, Reset, Serviced<Long> {

	private static final String LIST = "List";

	private static final String DATA_DUMP = "Data Dump";

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<CreditNote> readOnlyService;

	@Autowired
	private SavingService<CreditNote> savingService;

	@Autowired
	private SpunService<CreditNote, Long> spunService;

	private CreditNote creditNote;

	private String excelVersion;

	public CreditNoteService() {
		reset();
	}

	public CreditNotePayment createPayment(LocalDate d, String reference, BigDecimal payment, String remarks) {
		CreditNotePayment p = new CreditNotePayment();
		p.setPaymentDate(d);
		p.setReference(reference);
		p.setPaymentValue(payment);
		p.setPaymentRemarks(remarks);
		return p;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CreditNote find(Long id) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/" + id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public CreditNote get() {
		if (creditNote == null)
			reset();
		return creditNote;
	}

	@Override
	public String getAlternateName() {
		return "C/N";
	}

	public BigDecimal getBalance() {
		BigDecimal v = get().getBalanceValue();
		return v != null ? v : ZERO;
	}

	@Override
	public String getCreatedBy() {
		return get().getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return get().getCreatedOn();
	}

	public List<CreditNoteDump> getDataDump() {
		return list().stream().flatMap(c -> toCreditNoteDumpList(c).stream()).collect(toList());
	}

	@Override
	public String getHeaderText() {
		return "Credit Note";
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	@Override
	public String getModule() {
		return "creditNote";
	}

	public String getPartiallyPaidHeaderText() {
		return getHeaderText() + " " + LIST;
	}

	public BigDecimal getPayment() {
		return getTotal().subtract(getBalance());
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<CreditNote> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<CreditNote> getSavingService() {
		return savingService;
	}

	@Override
	public SpunService<CreditNote, Long> getSpunService() {
		return spunService;
	}

	public BigDecimal getTotal() {
		BigDecimal v = get().getTotalValue();
		return v != null ? v : ZERO;
	}

	@Override
	public void reset() {
		set(new CreditNote());
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		tables[0].setId(excelName());
		excel.filename(excelFilename()).sheetname(excelVersion).table(tables).write();
	}

	public void saveDataDumpAsExcel(Tabular t) throws IOException {
		t.items(getDataDump());
		excelVersion = DATA_DUMP;
		saveAsExcel(t);
	}

	public void saveListAsExcel(Tabular t) throws IOException {
		t.items(list());
		excelVersion = LIST;
		saveAsExcel(t);
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		creditNote = (CreditNote) t;
	}

	public void updatePayments(List<CreditNotePayment> l) {
		get().setPayments(l);
		get().setBalanceValue(getTotal().subtract(payment()));
	}

	public void updateTotals(BigDecimal total) {
		get().setTotalValue(total);
		get().setBalanceValue(total);
	}

	private List<CreditNoteDump> creditNoteDumpList(CreditNote c) {
		return c.getPayments().stream().map(p -> creditNoteWithPaymentDump(c, p)).collect(toList());
	}

	private CreditNoteDump creditNoteOnlyDump(CreditNote c) {
		CreditNoteDump d = new CreditNoteDump();
		d.setId(c.getId());
		d.setCreditDate(c.getCreditDate());
		d.setDescription(c.getDescription());
		d.setTotalValue(c.getTotalValue());
		d.setBalanceValue(c.getBalanceValue());
		d.setRemarks(c.getRemarks());
		return d;
	}

	private CreditNoteDump creditNoteWithPaymentDump(CreditNote c, CreditNotePayment p) {
		CreditNoteDump d = creditNoteOnlyDump(c);
		d.setPaymentDate(p.getPaymentDate());
		d.setReference(p.getReference());
		d.setPaymentValue(p.getPaymentValue());
		d.setPaymentRemarks(p.getPaymentRemarks());
		return d;
	}

	private String excelFilename() {
		return excelName().replace(" ", ".").replace("/", "-");
	}

	private String excelName() {
		return getHeaderText() + " " + excelVersion + " as of " + toDateDisplay(now());
	}

	private BigDecimal payment() {
		List<CreditNotePayment> l = get().getPayments();
		return l == null ? ZERO : l.stream().map(CreditNotePayment::getPaymentValue).reduce(ZERO, BigDecimal::add);
	}

	private List<CreditNoteDump> toCreditNoteDumpList(CreditNote c) {
		if (c.getPayments().isEmpty())
			return asList(creditNoteOnlyDump(c));
		return creditNoteDumpList(c);
	}
}
