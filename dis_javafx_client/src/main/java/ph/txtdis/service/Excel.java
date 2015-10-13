package ph.txtdis.service;

import ph.txtdis.excel.Tabular;

public interface Excel<T> extends Listed<T> {

	void saveAsExcel(Tabular... tables) throws Exception;
}
