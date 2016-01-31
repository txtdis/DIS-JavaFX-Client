package ph.txtdis.service;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static ph.txtdis.util.NumberUtils.divide;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ZERO;

import static ph.txtdis.util.DateTimeUtils.endOfMonth;
import static ph.txtdis.util.DateTimeUtils.startOfMonth;
import static ph.txtdis.util.DateTimeUtils.toDottedYearMonth;
import static ph.txtdis.util.DateTimeUtils.toFullMonthYear;
import static ph.txtdis.util.DateTimeUtils.toLongMonthYear;

import ph.txtdis.dto.Vat;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Service("vatService")
public class VatService implements Iconed, Spreadsheet<Vat>, Spun {

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<Vat> readOnlyService;

	private BigDecimal vatRate;

	private List<Vat> list;

	private LocalDate date;

	public LocalDate getDate() {
		if (date == null)
			date = now();
		return date;
	}

	@Override
	public String getHeaderText() {
		return "Value-Added Tax";
	}

	@Override
	public String getModule() {
		return "vat";
	}

	@Override
	public ReadOnlyService<Vat> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public String getSubhead() {
		return toFullMonthYear(getDate());
	}

	@Override
	public String getTitleText() {
		return getAllCapModule() + " " + toLongMonthYear(getDate());
	}

	@Override
	public List<BigDecimal> getTotals() {
		return asList(getTotalInvoiceValue(), getTotalVatValue());
	}

	@Override
	public List<Vat> list() {
		try {
			List<Vat> l = readOnlyService.module(getModule()).getList("/list?start=" + start() + "&end=" + end());
			return list = (l != null ? l : emptyList());
		} catch (Exception e) {
			e.printStackTrace();
			return list = emptyList();
		}
	}

	@Override
	public void next() {
		date = getDate().plusMonths(1L);
	}

	@Override
	public void previous() {
		date = getDate().minusMonths(1L);
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal vatRate() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return vatRate == null ? computeVat() : vatRate;
	}

	private BigDecimal computeVat() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		Vat v = readOnlyService.module(getModule()).getOne("/rate");
		BigDecimal vat = v.getVatValue();
		return divide(vat, (v.getValue().subtract(vat)));
	}

	private LocalDate end() {
		return endOfMonth(getDate());
	}

	private String getAllCapModule() {
		return "VAT";
	}

	private String getExcelFileName() {
		return getAllCapModule() + "." + toDottedYearMonth(getDate());
	}

	private String getExcelSheetName() {
		return toLongMonthYear(getDate());
	}

	private BigDecimal getTotalInvoiceValue() {
		return list.stream().filter(v -> v.getValue() != null).map(v -> v.getValue()).reduce(ZERO, BigDecimal::add);
	}

	private BigDecimal getTotalVatValue() {
		return list.stream().filter(v -> v.getVatValue() != null).map(v -> v.getVatValue()).reduce(ZERO,
				BigDecimal::add);
	}

	private LocalDate start() {
		return startOfMonth(getDate());
	}
}
