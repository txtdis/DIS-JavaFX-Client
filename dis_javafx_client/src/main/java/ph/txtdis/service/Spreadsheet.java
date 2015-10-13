package ph.txtdis.service;

public interface Spreadsheet<T> extends Totaled, Excel<T> {

	String getHeaderText();

	String getSubheaderText();

	String getTitleText();
}
