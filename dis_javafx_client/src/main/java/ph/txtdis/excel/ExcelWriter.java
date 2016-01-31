package ph.txtdis.excel;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static ph.txtdis.util.Reflection.invokeMethod;
import static ph.txtdis.util.TextUtils.toBoolSign;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.endsWith;

import static ph.txtdis.util.DateTimeUtils.toUtilDate;

@Scope("prototype")
@Component("excelWriter")
public class ExcelWriter {

	private int colIdx;

	private ArrayList<String> getters;

	private HSSFWorkbook wb;

	private List<List<Tabular>> tables;

	private Sheet sheet;

	private String[] sheetnames;

	private String filename;

	private Tabular table;

	private TabularColumn column;

	@Autowired
	private ExcelStyle s;

	public ExcelWriter filename(String filename) {
		this.filename = filename;
		return this;
	}

	public ExcelWriter sheetname(String... sheetnames) {
		this.sheetnames = sheetnames;
		return this;
	}

	@SuppressWarnings("unchecked")
	public ExcelWriter table(List<Tabular>... tables) {
		this.tables = asList(tables);
		return this;
	}

	public ExcelWriter table(Tabular... tables) {
		this.tables = asList(asList(tables));
		return this;
	}

	public void write() throws IOException {
		setup();
		writeWorkbook(file());
		openWorkbook(file());
	}

	private void addCell(CellStyle header, Row row, int i) {
		Cell c = row.createCell(i);
		c.setCellValue(column.getText());
		c.setCellStyle(header);
	}

	private void addHeader() {
		List<?> columns = table.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			column = (TabularColumn) columns.get(i);
			sheet.setColumnWidth(i + colIdx, getWidth());
			addCell(s.header(), getRow(1), i + colIdx);
			getters.add("get" + capitalize(column.getId()));
		}
	}

	private void addSummationIfRequired() {
		List<BigDecimal> totals = table.getColumnTotals();
		for (int i = totals.size() - 1; i >= 0; i--)
			setValue(totals.get(i), addSumSuffixToGetterMethodName(getters, i), createSumCell(i));
	}

	private String addSumSuffixToGetterMethodName(ArrayList<String> getters, int i) {
		return getters.get(i + table.getColumnIndexOfFirstTotal()) + "Sum";
	}

	private void addTitle() {
		sheet.addMergedRegion(new CellRangeAddress(0, 0, colIdx, colIdx + table.getColumnCount() - 1));
		Row row = getRow(0);
		row.setHeightInPoints(30);
		setTitleCell(row);
	}

	private void create() {
		for (int i = 0; i < sheetnames.length; i++) {
			createSheet(sheetnames[i]);
			colIdx = 0;
			for (Tabular table : tables.get(i)) {
				this.table = table;
				getters = new ArrayList<>();
				addTitle();
				addHeader();
				populateRows();
				colIdx += table.getColumnCount();
				sheet.setColumnWidth(colIdx++, 750);
			}
		}
	}

	private void createSheet(String id) {
		sheet = wb.createSheet(id);
		// sheet.protectSheet("secretPassword");
		sheet.createFreezePane(0, 2, 0, 2);
	}

	private Cell createSumCell(int i) {
		return getLastRow().createCell(i + colIdx + table.getColumnIndexOfFirstTotal());
	}

	private String file() {
		return getProperty("user.home") + "\\Desktop\\" + filename + ".xls";
	}

	private Row getLastRow() {
		return getRow(table.getLastRowIndex() + 2);
	}

	private Row getRow(int rowIdx) {
		Row row = sheet.getRow(rowIdx);
		if (row == null)
			row = sheet.createRow(rowIdx);
		return row;
	}

	private Object getValue(Object item, String method) {
		return invokeMethod(item, method);
	}

	private int getWidth() {
		return (int) (column.getWidth() / 10 + 2) * 256;
	}

	private void openWorkbook(String file) throws IOException {
		String[] cmd = new String[] { "cmd.exe", "/C", file };
		getRuntime().exec(cmd);
	}

	private void populateColumns(Object item, Row row) {
		for (int i = 0; i < getters.size(); i++)
			setValue(getValue(item, getters.get(i)), getters.get(i), row.createCell(i + colIdx));
	}

	private void populateRows() {
		List<?> items = table.getItems();
		for (int i = 0; i < items.size(); i++)
			populateColumns(items.get(i), getRow(i + 2));
		addSummationIfRequired();
	}

	private void setBoolean(Cell c, boolean b) {
		c.setCellValue(toBoolSign(b));
		c.setCellStyle(s.center());
	}

	private void setCenter(Cell c, String t) {
		c.setCellValue(t);
		c.setCellStyle(s.center());
	}

	private void setCurrency(Cell c, BigDecimal d) {
		c.setCellValue(d.doubleValue());
		c.setCellStyle(s.currency());
	}

	private void setCurrencySum(Cell c, BigDecimal d) {
		c.setCellValue(d.doubleValue());
		c.setCellStyle(s.currencySum());
	}

	private void setDate(Cell c, LocalDate d) {
		c.setCellValue(toUtilDate(d));
		c.setCellStyle(s.date());
	}

	private void setDecimal(Cell c, BigDecimal d) {
		c.setCellValue(d.doubleValue());
		c.setCellStyle(s.decimal());
	}

	private void setId(Cell c, long l) {
		c.setCellValue(l);
		c.setCellStyle(s.id());
	}

	private void setInteger(Cell c, int i) {
		c.setCellValue(i);
		c.setCellStyle(s.integer());
	}

	private void setQty(Cell c, BigDecimal d) {
		c.setCellValue(d.doubleValue());
		c.setCellStyle(s.integer());
	}

	private void setQtySum(Cell c, BigDecimal d) {
		c.setCellValue(d.doubleValue());
		c.setCellStyle(s.decimalSum());
	}

	private void setRight(Cell c, String t) {
		c.setCellValue(t);
		c.setCellStyle(t.contains(">") ? s.red() : s.right());
	}

	private void setText(Cell c, String t) {
		c.setCellValue(t);
		c.setCellStyle(t.contains(">") ? s.red() : s.left());
	}

	private void setTitleCell(Row row) {
		Cell c = row.createCell(colIdx);
		c.setCellValue(table.getId());
		c.setCellStyle(s.title());
	}

	private void setup() {
		wb = new HSSFWorkbook();
		s.add(wb);
		create();
	}

	private void setValue(Object o, String s, Cell c) {
		if (o == null)
			return;
		if (endsWith(s, "Id"))
			setId(c, (long) o);
		else if (endsWith(s, "Count"))
			setInteger(c, (int) o);
		else if (endsWith(s, "Vol"))
			setDecimal(c, (BigDecimal) o);
		else if (endsWith(s, "ValueSum"))
			setCurrencySum(c, (BigDecimal) o);
		else if (endsWith(s, "QtySum"))
			setQtySum(c, (BigDecimal) o);
		else if (endsWith(s, "Value"))
			setCurrency(c, (BigDecimal) o);
		else if (endsWith(s, "Qty"))
			setQty(c, (BigDecimal) o);
		else if (endsWith(s, "Date"))
			setDate(c, (LocalDate) o);
		else if (endsWith(s, "Type"))
			setCenter(c, o.toString());
		else if (endsWith(s, "Level"))
			setRight(c, o.toString());
		else if (endsWith(s, "Valid") || endsWith(s, "Paid"))
			setBoolean(c, (boolean) o);
		else
			setText(c, o.toString());
	}

	private void writeWorkbook(String f) throws IOException {
		FileOutputStream s = new FileOutputStream(f);
		wb.write(s);
		s.close();
	}
}