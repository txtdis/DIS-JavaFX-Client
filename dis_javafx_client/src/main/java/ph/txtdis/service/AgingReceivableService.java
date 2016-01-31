package ph.txtdis.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static ph.txtdis.util.DateTimeUtils.toTimestampFilename;
import static ph.txtdis.util.DateTimeUtils.toTimestampText;

import ph.txtdis.dto.AgingReceivable;
import ph.txtdis.dto.AgingReceivableReport;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;

@Scope("prototype")
@Service("agingReceivableService")
public class AgingReceivableService
		implements SellerFiltered<AgingReceivable>, Spreadsheet<AgingReceivable>, TotaledTable
{

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<AgingReceivableReport> readOnlyService;

	private AgingReceivableReport report;

	public AgingReceivableService() {
		report = null;
	}

	@Override
	public String getHeaderText() {
		return "Aging Receivable List";
	}

	@Override
	public String getModule() {
		return "agingReceivable";
	}

	@Override
	public ReadOnlyService<AgingReceivable> getReadOnlyService() {
		return null;
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
	public List<AgingReceivable> list() {
		if (report == null)
			report = generateReport();
		List<AgingReceivable> list = report.getReceivables();
		return list != null ? list : new ArrayList<>();
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(getExcelSheetName()).table(tables).write();
	}

	private AgingReceivableReport generateReport() {
		try {
			return readOnlyService.module(getModule()).getOne("");
		} catch (Exception e) {
			e.printStackTrace();
			return new AgingReceivableReport();
		}
	}

	private String getExcelFileName() {
		return "Aging.Receivables." + getExcelSheetName();
	}

	private String getExcelSheetName() {
		return toTimestampFilename(getTimestamp());
	}
}
