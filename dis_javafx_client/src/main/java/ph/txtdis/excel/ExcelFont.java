package ph.txtdis.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;

public class ExcelFont {

	private HSSFWorkbook wb;

	public ExcelFont(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public Font title() {
		Font f = wb.createFont();
		f.setFontName("Calibri");
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 15);
		return f;
	}

	public Font bold() {
		Font f = wb.createFont();
		f.setFontName("Calibri");
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 11);
		return f;
	}

	public Font normal() {
		Font f = wb.createFont();
		f.setFontName("Calibri");
		f.setFontHeightInPoints((short) 11);
		return f;
	}

	public Font red() {
		Font f = wb.createFont();
		f.setFontName("Calibri");
		f.setFontHeightInPoints((short) 11);
		f.setColor(HSSFColor.RED.index);
		return f;
	}
}