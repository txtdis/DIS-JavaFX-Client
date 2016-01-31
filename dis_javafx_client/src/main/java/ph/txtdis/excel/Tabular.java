package ph.txtdis.excel;

import java.math.BigDecimal;
import java.util.List;

public interface Tabular {

	int getColumnCount();

	int getColumnIndexOfFirstTotal();

	List<?> getColumns();

	List<BigDecimal> getColumnTotals();

	String getId();

	List<?> getItems();

	int getLastRowIndex();

	void items(List<?> items);

	void setId(String id);
}
