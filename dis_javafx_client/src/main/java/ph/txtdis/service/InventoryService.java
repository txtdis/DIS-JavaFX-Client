package ph.txtdis.service;

import static java.time.ZonedDateTime.now;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.capitalize;

import static java.math.BigDecimal.ZERO;

import static ph.txtdis.util.DateTimeUtils.toTimestampFilename;
import static ph.txtdis.util.DateTimeUtils.toTimestampText;

import ph.txtdis.dto.Inventory;
import ph.txtdis.excel.ExcelWriter;
import ph.txtdis.excel.Tabular;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Service("inventoryService")
public class InventoryService implements Iconed, Spreadsheet<Inventory> {

	@Autowired
	private ExcelWriter excel;

	@Autowired
	private ReadOnlyService<Inventory> readOnlyService;

	private List<Inventory> list;

	private LocalDate date;

	public LocalDate getDate() {
		if (date == null)
			date = LocalDate.now();
		return date;
	}

	@Override
	public String getHeaderText() {
		return "Inventory";
	}

	public Inventory getInventory(Long itemId) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/item?id=" + itemId);
	}

	@Override
	public String getModule() {
		return "inventory";
	}

	@Override
	public ReadOnlyService<Inventory> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public String getSubhead() {
		return "As of " + toTimestampText(now());
	}

	@Override
	public String getTitleText() {
		return capitalize(getModule());
	}

	@Override
	public List<BigDecimal> getTotals() {
		return asList(getTotalValue(), getTotalObsolesenceValue());
	}

	@Override
	public List<Inventory> list() {
		return list = Spreadsheet.super.list();
	}

	@Override
	public void saveAsExcel(Tabular... tables) throws IOException {
		excel.filename(getExcelFileName()).sheetname(toTimestampFilename(now())).table(tables).write();
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	private String getExcelFileName() {
		return getTitleText() + "." + toTimestampFilename(now());
	}

	private BigDecimal getTotalObsolesenceValue() {
		return list.stream().filter(v -> v.getObsolesenceValue() != null).map(v -> v.getObsolesenceValue()).reduce(ZERO,
				(a, b) -> a.add(b));
	}

	private BigDecimal getTotalValue() {
		return list.stream().filter(v -> v.getValue() != null).map(v -> v.getValue()).reduce(ZERO, (a, b) -> a.add(b));
	}
}
