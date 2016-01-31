package ph.txtdis.service;

import java.io.IOException;

import ph.txtdis.excel.Tabular;

public interface Excel<T> extends Listed<T> {

	void saveAsExcel(Tabular... tables) throws IOException;
}
