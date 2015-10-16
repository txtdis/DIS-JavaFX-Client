package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Vat;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.util.Numeric;
import ph.txtdis.util.Temporal;

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
	public String getSubheaderText() {
		return Temporal.toFullMonthYear(getDate());
	}

	@Override
	public String getTitleText() {
		return getAllCapModule() + " " + Temporal.toLongMonthYear(getDate());
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
		return Numeric.divide(vat, (v.getValue().subtract(vat)));
	}

	private LocalDate end() {
		return Temporal.endOfMonth(getDate());
	}

	private String getAllCapModule() {
		return "VAT";
	}

	private String getExcelFileName() {
		return getAllCapModule() + "." + Temporal.toFileMonthYear(getDate());
	}

	private String getExcelSheetName() {
		return Temporal.toLongMonthYear(getDate());
	}

	private BigDecimal getTotalInvoiceValue() {
		return list.stream().map(v -> v.getValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	private BigDecimal getTotalVatValue() {
		return list.stream().map(v -> v.getVatValue()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	private LocalDate start() {
		return Temporal.startOfMonth(getDate());
	}
}
