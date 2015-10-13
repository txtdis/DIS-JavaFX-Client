package ph.txtdis.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ExcelStyle {

	private HSSFWorkbook wb;
	private Font normal, red, title, bold;
	private short id, integer, date, decimal, currency;

	public ExcelStyle(HSSFWorkbook wb) {
		this.wb = wb;
		setFont();
		setFormat();
	}

	private void setFormat() {
		ExcelFormat f = new ExcelFormat(wb);
		id = f.id();
		integer = f.integer();
		date = f.date();
		decimal = f.decimal();
		currency = f.currency();
	}

	private void setFont() {
		ExcelFont font = new ExcelFont(wb);
		normal = font.normal();
		red = font.red();
		title = font.title();
		bold = font.bold();
	}

	public CellStyle title() {
		CellStyle s = wb.createCellStyle();
		s.setFont(title);
		s.setAlignment(CellStyle.ALIGN_LEFT);
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		s.setWrapText(true);
		s.setLocked(true);
		return s;
	}

	public CellStyle header() {
		CellStyle s = wb.createCellStyle();
		s.setBorderRight(CellStyle.BORDER_THIN);
		s.setBorderLeft(CellStyle.BORDER_THIN);
		s.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		s.setFillPattern(CellStyle.SOLID_FOREGROUND);
		s.setFont(bold);
		s.setAlignment(CellStyle.ALIGN_CENTER);
		s.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		s.setLocked(true);
		s.setWrapText(true);
		return s;
	}

	public CellStyle integer() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(integer);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle id() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(id);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle decimal() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(decimal);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle decimalSum() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(decimal);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setBorderTop(CellStyle.BORDER_THIN);
		s.setBorderBottom(CellStyle.BORDER_DOUBLE);
		s.setLocked(true);
		return s;
	}

	public CellStyle red() {
		CellStyle s = wb.createCellStyle();
		s.setFont(red);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle right() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle center() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setAlignment(CellStyle.ALIGN_CENTER);
		s.setLocked(true);
		return s;
	}

	public CellStyle left() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setAlignment(CellStyle.ALIGN_LEFT);
		s.setLocked(true);
		return s;
	}

	public CellStyle currency() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(currency);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setLocked(true);
		return s;
	}

	public CellStyle currencySum() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(currency);
		s.setAlignment(CellStyle.ALIGN_RIGHT);
		s.setBorderTop(CellStyle.BORDER_THIN);
		s.setBorderBottom(CellStyle.BORDER_DOUBLE);
		s.setLocked(true);
		return s;
	}

	public CellStyle date() {
		CellStyle s = wb.createCellStyle();
		s.setFont(normal);
		s.setDataFormat(date);
		s.setAlignment(CellStyle.ALIGN_CENTER);
		s.setLocked(true);
		return s;
	}
}
