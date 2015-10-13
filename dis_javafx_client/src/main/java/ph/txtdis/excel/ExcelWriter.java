package ph.txtdis.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import ph.txtdis.util.Reflection;
import ph.txtdis.util.Temporal;
import ph.txtdis.util.Text;

@Component
public class ExcelWriter {

	private int colIdx;

	private ArrayList<String> getters;
	private CellStyle title, header, right, red, center, left, id, integer, decimal, decimalSum, currency, currencySum,
			date;
	private HSSFWorkbook workbook;
	private List<List<Tabular>> tables;
	private Sheet sheet;
	private String[] sheetnames;
	private String filename;
	private Tabular table;
	private TabularColumn column;

	public ExcelWriter table(Tabular... tables) {
		this.tables = Arrays.asList(Arrays.asList(tables));
		return this;
	}

	@SuppressWarnings("unchecked")
	public ExcelWriter table(List<Tabular>... tables) {
		this.tables = Arrays.asList(tables);
		return this;
	}

	public ExcelWriter sheetname(String... sheetnames) {
		this.sheetnames = sheetnames;
		return this;
	}

	public ExcelWriter filename(String filename) {
		this.filename = filename;
		return this;
	}

	public void write() throws IOException {
		setup();
		writeWorkbook(file());
		openWorkbook(file());
	}

	private String file() {
		return System.getProperty("user.home") + "\\Desktop\\" + filename + ".xls";
	}

	private void setup() {
		workbook = new HSSFWorkbook();
		setCellStyles();
		create();
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
		sheet = workbook.createSheet(id);
		sheet.protectSheet("secretPassword");
		sheet.createFreezePane(0, 2, 0, 2);
	}

	private void setCellStyles() {
		ExcelStyle s = new ExcelStyle(workbook);
		title = s.title();
		header = s.header();
		right = s.right();
		red = s.red();
		center = s.center();
		left = s.left();
		id = s.id();
		integer = s.integer();
		decimal = s.decimal();
		decimalSum = s.decimalSum();
		currency = s.currency();
		currencySum = s.currencySum();
		date = s.date();
	}

	private void addTitle() {
		sheet.addMergedRegion(new CellRangeAddress(0, 0, colIdx, colIdx + table.getColumnCount() - 1));
		Row row = getRow(0);
		row.setHeightInPoints(30);
		setTitleCell(row);
	}

	private void setTitleCell(Row row) {
		Cell c = row.createCell(colIdx);
		c.setCellValue(table.getId());
		c.setCellStyle(title);
	}

	private void addHeader() {
		List<?> columns = table.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			column = (TabularColumn) columns.get(i);
			sheet.setColumnWidth(i + colIdx, getWidth());
			addCell(header, getRow(1), i + colIdx);
			getters.add("get" + StringUtils.capitalize(column.getId()));
		}
	}

	private int getWidth() {
		return (int) (column.getWidth() / 10 + 2) * 256;
	}

	private Row getRow(int rowIdx) {
		Row row = sheet.getRow(rowIdx);
		if (row == null)
			row = sheet.createRow(rowIdx);
		return row;
	}

	private void addCell(CellStyle header, Row row, int i) {
		Cell c = row.createCell(i);
		c.setCellValue(column.getText());
		c.setCellStyle(header);
	}

	private void populateRows() {
		List<?> items = table.getItems();
		for (int i = 0; i < items.size(); i++)
			populateColumns(items.get(i), getRow(i + 2));
		addSummationIfRequired();
	}

	private void addSummationIfRequired() {
		List<BigDecimal> totals = table.getColumnTotals();
		for (int i = totals.size() - 1; i >= 0; i--)
			setValue(totals.get(i), addSumSuffixToGetterMethodName(getters, i), createSumCell(i));
	}

	private Cell createSumCell(int i) {
		return getLastRow().createCell(i + colIdx + table.getColumnIndexOfFirstTotal());
	}

	private String addSumSuffixToGetterMethodName(ArrayList<String> getters, int i) {
		return getters.get(i + table.getColumnIndexOfFirstTotal()) + "Sum";
	}

	private Row getLastRow() {
		return getRow(table.getLastRowIndex() + 2);
	}

	private void populateColumns(Object item, Row row) {
		for (int i = 0; i < getters.size(); i++)
			setValue(getValue(item, getters.get(i)), getters.get(i), row.createCell(i + colIdx));
	}

	private Object getValue(Object item, String method) {
		return Reflection.invokeMethod(item, method);
	}

	private void setValue(Object o, String getter, Cell c) {
		if (StringUtils.endsWith(getter, "Id"))
			setIdValue(c, o);
		else if (StringUtils.endsWith(getter, "Count"))
			setIntValue(c, o);
		else if (StringUtils.endsWith(getter, "Vol"))
			setDecimalValue(c, o);
		else if (StringUtils.endsWith(getter, "ValueSum"))
			setCurrencySum(c, o);
		else if (StringUtils.endsWith(getter, "QtySum"))
			setQtySum(c, o);
		else if (StringUtils.endsWith(getter, "Value"))
			setCurrencyValue(c, o);
		else if (StringUtils.endsWith(getter, "Qty"))
			setQtyValue(c, o);
		else if (StringUtils.endsWith(getter, "Date"))
			setDateValue(c, o);
		else if (StringUtils.endsWith(getter, "Type"))
			setCenterValue(c, o);
		else if (StringUtils.endsWith(getter, "Level"))
			setRightValue(c, o);
		else
			setTextValue(c, o);
	}

	private void setIdValue(Cell c, Object o) {
		c.setCellValue((long) o);
		c.setCellStyle(id);
	}

	private void setIntValue(Cell c, Object o) {
		c.setCellValue((int) o);
		c.setCellStyle(integer);
	}

	private void setDecimalValue(Cell c, Object o) {
		double qty = ((BigDecimal) o).doubleValue();
		c.setCellValue(qty);
		c.setCellStyle(decimal);
	}

	private void setQtyValue(Cell c, Object o) {
		double qty = ((BigDecimal) o).doubleValue();
		c.setCellValue(qty);
		c.setCellStyle(integer);
	}

	private void setQtySum(Cell c, Object o) {
		double qty = ((BigDecimal) o).doubleValue();
		c.setCellValue(qty);
		c.setCellStyle(decimalSum);
	}

	private void setCurrencyValue(Cell c, Object o) {
		double d = ((BigDecimal) o).doubleValue();
		c.setCellValue(d);
		c.setCellStyle(currency);
	}

	private void setCurrencySum(Cell c, Object o) {
		double d = ((BigDecimal) o).doubleValue();
		c.setCellValue(d);
		c.setCellStyle(currencySum);
	}

	private void setDateValue(Cell c, Object o) {
		c.setCellValue(Temporal.toUtilDate((LocalDate) o));
		c.setCellStyle(date);
	}

	private void setCenterValue(Cell c, Object o) {
		c.setCellValue(Text.toString(o));
		c.setCellStyle(center);
	}

	private void setRightValue(Cell c, Object o) {
		String text = Text.toString(o);
		c.setCellValue(text);
		c.setCellStyle(text.contains(">") ? red : right);
	}

	private void setTextValue(Cell c, Object o) {
		String text = Text.toString(o);
		c.setCellValue(text);
		c.setCellStyle(text.contains(">") ? red : left);
	}

	private void writeWorkbook(String file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();
	}

	private void openWorkbook(String file) throws IOException {
		String[] cmd = new String[] { "cmd.exe", "/C", file };
		Runtime.getRuntime().exec(cmd);
	}
}