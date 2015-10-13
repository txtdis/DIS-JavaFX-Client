package ph.txtdis.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;

public class ExcelFormat {

	private DataFormat f;

	public ExcelFormat(HSSFWorkbook wb) {
		CreationHelper ch = wb.getCreationHelper();
		f = ch.createDataFormat();
	}

	public short integer() {
		return f.getFormat("_(#,##0_);[Red]_((#,##0);_(\"-\"??_);_(@_)");
	}

	public short decimal() {
		return f.getFormat("_(#,##0.00_);[Red]_((#,##0.00);_(\"-\"??_);_(@_)");
	}

	public short currency() {
		return f.getFormat(
		        "_(₱* #,##0.00_);[Red]_(₱* (#,##0.00);_(₱* \"-\"??_);_(@_)");
	}

	public short date() {
		return f.getFormat("m/d/yy;@");
	}

	public short id() {
		return f.getFormat("###0");
	}
}
