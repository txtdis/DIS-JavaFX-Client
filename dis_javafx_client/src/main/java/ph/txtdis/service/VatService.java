package ph.txtdis.service;

import static ph.txtdis.util.DateTimeUtils.endOfMonth;
import static ph.txtdis.util.DateTimeUtils.startOfMonth;
import static ph.txtdis.util.DateTimeUtils.toDottedYearMonth;
import static ph.txtdis.util.DateTimeUtils.toFullMonthYear;
import static ph.txtdis.util.DateTimeUtils.toLongMonthYear;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ZERO;

import ph.txtdis.dto.Vat;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.util.NumberUtils;

@Service("VatService")
public class VatService implements Spreadsheet<Vat>, Spun {

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<Vat> readOnlyService;

	private BigDecimal vatRate;

	private List<Vat> list;

	private LocalDate date;

	public LocalDate getDate() {
		if (date == null)
			date = LocalDate.now();
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
	public String getSubhead() {
		return toFullMonthYear(getDate());
	}

	@Override
	public String getTitleText() {
		return getAllCapModule() + " " + toLongMonthYear(getDate());
	}

	@Override
	public List<BigDecimal> getTotals() {
		return Arrays.asList(getTotalInvoiceValue(), getTotalVatValue());
	}

	@Override
	public List<Vat> list() throws Exception {
		list = readOnlyService.module(getModule()).getList("/list?start=" + start() + "&end=" + end());
		return list != null ? list : Collections.emptyList();
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
	public void saveAsExcel(Tabular... tables) throws Exception {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal vatRate() throws Exception {
		return vatRate == null ? computeVat() : vatRate;
	}

	private BigDecimal computeVat() throws Exception {
		Vat v = readOnlyService.module(getModule()).getOne("/rate");
		BigDecimal vat = v.getVatValue();
		return NumberUtils.divide(vat, (v.getValue().subtract(vat)));
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
		return list.stream().filter(v -> v.getValue() != null).map(v -> v.getValue()).reduce(ZERO, (a, b) -> a.add(b));
	}

	private BigDecimal getTotalVatValue() {
		return list.stream().filter(v -> v.getVatValue() != null).map(v -> v.getVatValue()).reduce(ZERO,
				(a, b) -> a.add(b));
	}

	private LocalDate start() {
		return startOfMonth(getDate());
	}
}
