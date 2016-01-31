package ph.txtdis.service;

import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.math.BigDecimal.ZERO;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import ph.txtdis.dto.Billable;
import ph.txtdis.dto.SalesRevenue;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;

@Service("salesRevenueService")
public class SalesRevenueService
		implements BilledAllPickedSalesOrder, Iconed, Spreadsheet<SalesRevenue>, SellerFiltered<SalesRevenue>, Spun
{

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<SalesRevenue> readOnlyService;

	@Autowired
	private ReadOnlyService<Billable> billableReadOnlyService;

	private LocalDate start, end;

	@Override
	public ReadOnlyService<Billable> getBillableReadOnlyService() {
		return billableReadOnlyService;
	}

	public LocalDate getEndDate() {
		if (end == null)
			end = yesterday();
		return end;
	}

	@Override
	public String getHeaderText() {
		return "Sales Revenue";
	}

	@Override
	public String getModule() {
		return "salesRevenue";
	}

	@Override
	public ReadOnlyService<SalesRevenue> getReadOnlyService() {
		return readOnlyService;
	}

	public LocalDate getStartDate() {
		if (start == null)
			start = yesterday();
		return start;
	}

	@Override
	public String getSubhead() {
		String d = toDateDisplay(getStartDate());
		if (!start.isEqual(getEndDate()))
			d = d + " - " + toDateDisplay(end);
		return d;
	}

	@Override
	public String getTitleText() {
		return "S/R: " + getSubhead();
	}

	@Override
	public List<BigDecimal> getTotals() {
		BigDecimal t = list().stream().filter(v -> v.getValue() != null).map(v -> v.getValue()).reduce(ZERO,
				BigDecimal::add);
		return asList(t);
	}

	@Override
	public List<SalesRevenue> list() {
		try {
			return readOnlyService.module(getModule())
					.getList("/list?start=" + getStartDate() + "&end=" + getEndDate());
		} catch (Exception e) {
			e.printStackTrace();
			return emptyList();
		}
	}

	@Override
	public void next() {
		if (getEndDate().isBefore(yesterday()))
			start = end = getEndDate().plusDays(1L);
	}

	@Override
	public void previous() {
		end = start = getStartDate().minusDays(1L);
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(excelName()).sheetname(getHeaderText()).table(tables).write();
	}

	public void setEndDate(LocalDate d) {
		end = d;
	}

	public void setStartDate(LocalDate d) {
		start = d;
	}

	private String excelName() {
		return getHeaderText().replace(" ", ".") + "." + getSubhead().replace("-", ".to.").replace("/", "-");
	}

	private LocalDate yesterday() {
		return now().minusDays(1L);
	}
}
