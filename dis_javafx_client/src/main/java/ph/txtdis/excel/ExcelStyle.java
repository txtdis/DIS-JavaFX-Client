package ph.txtdis.excel;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;
import static org.apache.poi.ss.usermodel.CellStyle.BORDER_DOUBLE;
import static org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;
import static org.apache.poi.ss.usermodel.CellStyle.SOLID_FOREGROUND;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_CENTER;
import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;
import static org.apache.poi.ss.usermodel.IndexedColors.GREY_50_PERCENT;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component("excelStyle")
public class ExcelStyle {

	private static final String CALIBRI = "Calibri";

	private HSSFWorkbook wb;

	private Font normalFont, greenFont, redFont, titleFont, boldFont;

	private CellStyle bool, center, currency, currencySum, date, decimal, decimalSum, falseBool, header, id, integer,
			left, red, right, title, trueBool;

	private DataFormat format;

	public ExcelStyle add(HSSFWorkbook wb) {
		this.wb = wb;
		this.format = wb.getCreationHelper().createDataFormat();
		setAll();
		return this;
	}

	public CellStyle bool() {
		return bool;
	}

	public CellStyle center() {
		return center;
	}

	public CellStyle currency() {
		return currency;
	}

	public CellStyle currencySum() {
		return currencySum;
	}

	public CellStyle date() {
		return date;
	}

	public CellStyle decimal() {
		return decimal;
	}

	public CellStyle decimalSum() {
		return decimalSum;
	}

	public CellStyle falseBool() {
		return falseBool;
	}

	public CellStyle header() {
		return header;
	}

	public CellStyle id() {
		return id;
	}

	public CellStyle integer() {
		return integer;
	}

	public CellStyle left() {
		return left;
	}

	public CellStyle red() {
		return red;
	}

	public CellStyle right() {
		return right;
	}

	public CellStyle title() {
		return title;
	}

	public CellStyle trueBool() {
		return trueBool;
	}

	private Font boldFont() {
		return boldFont;
	}

	private short currencyFormat() {
		return format.getFormat("_(₱* #,##0.00_);[Red]_(₱* (#,##0.00);_(₱* \"-\"??_);_(@_)");
	}

	private short dateFormat() {
		return format.getFormat("m/d/yy;@");
	}

	private short decimalFormat() {
		return format.getFormat("_(#,##0.00_);[Red]_((#,##0.00);_(\"-\"??_);_(@_)");
	}

	private Font greenFont() {
		return greenFont;
	}

	private short idFormat() {
		return format.getFormat("###0");
	}

	private short integerFormat() {
		return format.getFormat("_(#,##0_);[Red]_((#,##0);_(\"-\"??_);_(@_)");
	}

	private Font normalFont() {
		return normalFont;
	}

	private Font redFont() {
		return redFont;
	}

	private void setAll() {
		setFonts();
		setStyles();
	}

	private void setBoldFont() {
		boldFont = wb.createFont();
		boldFont.setFontName(CALIBRI);
		boldFont.setBoldweight(BOLDWEIGHT_BOLD);
		boldFont.setFontHeightInPoints((short) 11);
	}

	private void setBool() {
		bool = wb.createCellStyle();
		bool.setFont(normalFont());
		bool.setAlignment(ALIGN_CENTER);
		bool.setLocked(true);
	}

	private void setCenter() {
		center = wb.createCellStyle();
		center.setFont(normalFont());
		center.setAlignment(ALIGN_CENTER);
		center.setLocked(true);
	}

	private void setCurrency() {
		currency = wb.createCellStyle();
		currency.setFont(normalFont());
		currency.setDataFormat(currencyFormat());
		currency.setAlignment(ALIGN_RIGHT);
		currency.setLocked(true);
	}

	private void setCurrencySum() {
		currencySum = wb.createCellStyle();
		currencySum.setFont(normalFont());
		currencySum.setDataFormat(currencyFormat());
		currencySum.setAlignment(ALIGN_RIGHT);
		currencySum.setBorderTop(BORDER_THIN);
		currencySum.setBorderBottom(BORDER_DOUBLE);
		currencySum.setLocked(true);
	}

	private void setDate() {
		date = wb.createCellStyle();
		date.setFont(normalFont());
		date.setDataFormat(dateFormat());
		date.setAlignment(ALIGN_CENTER);
		date.setLocked(true);
	}

	private void setDecimal() {
		decimal = wb.createCellStyle();
		decimal.setFont(normalFont());
		decimal.setDataFormat(decimalFormat());
		decimal.setAlignment(ALIGN_RIGHT);
		decimal.setLocked(true);
	}

	private void setDecimalSum() {
		decimalSum = wb.createCellStyle();
		decimalSum.setFont(normalFont());
		decimalSum.setDataFormat(decimalFormat());
		decimalSum.setAlignment(ALIGN_RIGHT);
		decimalSum.setBorderTop(BORDER_THIN);
		decimalSum.setBorderBottom(BORDER_DOUBLE);
		decimalSum.setLocked(true);
	}

	private void setFalseBool() {
		falseBool = wb.createCellStyle();
		falseBool.setFont(redFont());
		falseBool.setAlignment(ALIGN_CENTER);
		falseBool.setLocked(true);
	}

	private void setFonts() {
		setBoldFont();
		setGreenFont();
		setNormalFont();
		setRedFont();
		setTitleFont();
	}

	private void setGreenFont() {
		greenFont = wb.createFont();
		greenFont.setFontName(CALIBRI);
		greenFont.setFontHeightInPoints((short) 11);
		greenFont.setColor(HSSFColor.GREEN.index);
	}

	private void setHeader() {
		header = wb.createCellStyle();
		header.setBorderRight(BORDER_THIN);
		header.setBorderLeft(BORDER_THIN);
		header.setFillForegroundColor(GREY_50_PERCENT.getIndex());
		header.setFillPattern(SOLID_FOREGROUND);
		header.setFont(boldFont());
		header.setAlignment(ALIGN_CENTER);
		header.setVerticalAlignment(VERTICAL_CENTER);
		header.setLocked(true);
		header.setWrapText(true);
	}

	private void setId() {
		id = wb.createCellStyle();
		id.setFont(normalFont());
		id.setDataFormat(idFormat());
		id.setAlignment(ALIGN_RIGHT);
		id.setLocked(true);
	}

	private void setInteger() {
		integer = wb.createCellStyle();
		integer.setFont(normalFont());
		integer.setDataFormat(integerFormat());
		integer.setAlignment(ALIGN_RIGHT);
		integer.setLocked(true);
	}

	private void setLeft() {
		left = wb.createCellStyle();
		left.setFont(normalFont());
		left.setAlignment(ALIGN_LEFT);
		left.setLocked(true);
	}

	private void setNormalFont() {
		normalFont = wb.createFont();
		normalFont.setFontName(CALIBRI);
		normalFont.setFontHeightInPoints((short) 11);
	}

	private void setRed() {
		red = wb.createCellStyle();
		red.setFont(redFont());
		red.setAlignment(ALIGN_RIGHT);
		red.setLocked(true);
	}

	private void setRedFont() {
		redFont = wb.createFont();
		redFont.setFontName(CALIBRI);
		redFont.setFontHeightInPoints((short) 11);
		redFont.setColor(HSSFColor.RED.index);
	}

	private void setRight() {
		right = wb.createCellStyle();
		right.setFont(normalFont());
		right.setAlignment(ALIGN_RIGHT);
		right.setLocked(true);
	}

	private void setStyles() {
		setBool();
		setCenter();
		setCurrency();
		setCurrencySum();
		setDate();
		setDecimal();
		setDecimalSum();
		setFalseBool();
		setHeader();
		setId();
		setInteger();
		setLeft();
		setRed();
		setRight();
		setTitle();
		setTrueBool();
	}

	private void setTitle() {
		title = wb.createCellStyle();
		title.setFont(titleFont());
		title.setAlignment(ALIGN_LEFT);
		title.setVerticalAlignment(VERTICAL_CENTER);
		title.setWrapText(true);
		title.setLocked(true);
	}

	private void setTitleFont() {
		titleFont = wb.createFont();
		titleFont.setFontName(CALIBRI);
		titleFont.setBoldweight(BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short) 15);
	}

	private void setTrueBool() {
		trueBool = wb.createCellStyle();
		trueBool.setFont(greenFont());
		trueBool.setAlignment(ALIGN_CENTER);
		trueBool.setLocked(true);
	}

	private Font titleFont() {
		return titleFont;
	}
}
