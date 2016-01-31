package ph.txtdis.service;

public interface Spreadsheet<T> extends TotaledTable, Excel<T> {

	String getTitleText();
}
