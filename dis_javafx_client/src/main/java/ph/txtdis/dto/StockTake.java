package ph.txtdis.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockTake extends EntityCreationTracked<Long> {

	private String warehouse, checker, taker, remarks;

	private LocalDate countDate;

	private List<StockTakeDetail> details;
}
