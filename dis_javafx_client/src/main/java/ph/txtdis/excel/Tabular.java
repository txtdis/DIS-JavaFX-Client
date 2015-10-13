package ph.txtdis.excel;

import java.math.BigDecimal;
import java.util.List;

public interface Tabular {

	String getId();

	int getColumnCount();

	List<?> getColumns();

	List<?> getItems();

	List<BigDecimal> getColumnTotals();

	int getColumnIndexOfFirstTotal();

	int getLastRowIndex();
}
