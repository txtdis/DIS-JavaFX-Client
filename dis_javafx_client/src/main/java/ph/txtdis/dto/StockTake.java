package ph.txtdis.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockTake extends AbstractAuditedId<Long> {

	private Warehouse warehouse;

	private User taker;

	private User checker;

	private LocalDate stockTakeDate;

	private List<StockTakeDetail> details;
}
