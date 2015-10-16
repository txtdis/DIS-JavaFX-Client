package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.QualityType;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockTakeAdjustment extends AbstractTrackedId<Long> {

	private LocalDate stockTakeDate;

	private Item item;

	private QualityType quality;

	private BigDecimal qty;

	private String justification;
}
