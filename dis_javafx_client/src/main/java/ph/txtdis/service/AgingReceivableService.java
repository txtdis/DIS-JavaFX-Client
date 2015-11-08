package ph.txtdis.service;

import static ph.txtdis.util.DateTimeUtils.toTimestampText;
import static ph.txtdis.util.DateTimeUtils.toTimestampFilename;

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

@Service("agingReceivableService")
public class AgingReceivableService implements Spreadsheet<AgingReceivable>, TotaledTable {

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
	public String getModule() {
		return "agingReceivable";
	}

	@Override
	public String getSubhead() {
		return "A/R as of " + toTimestampText(getTimestamp());
	}

	public ZonedDateTime getTimestamp() {
		return report.getTimestamp();
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
		report = readOnlyService.module(getModule()).getOne("");
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
		return toTimestampFilename(getTimestamp());
	}
}
