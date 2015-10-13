package ph.txtdis.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.AgingReceivable;
import ph.txtdis.dto.AgingReceivableReport;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.util.Temporal;

@Service
public class AgingReceivableService implements Spreadsheet<AgingReceivable>, Totaled {

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<AgingReceivableReport> readOnlyService;

	private AgingReceivableReport report;

	@Override
	public String getHeaderText() {
		return "Aging Receivable List";
	}

	@Override
	public String getSubheaderText() {
		return "A/R as of " + Temporal.format(getTimestamp());
	}

	public ZonedDateTime getTimestamp() {
		return report == null ? null : report.getTimestamp();
	}

	@Override
	public String getTitleText() {
		return "Aging A/R";
	}

	@Override
	public List<BigDecimal> getTotals() {
		return report.getTotals();
	}

	@Override
	public List<AgingReceivable> list() throws Exception {
		report = readOnlyService.module("agingReceivable").getOne("");
		List<AgingReceivable> list = report.getReceivables();
		return list == null ? new ArrayList<>() : list;
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws Exception {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	private String getExcelFileName() {
		return "Aging.Receivables." + getExcelSheetName();
	}

	private String getExcelSheetName() {
		return Temporal.toFilename(getTimestamp());
	}
}
