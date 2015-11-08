package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Discount extends AbstractTrackedId<Long> implements StartDated {

	private int level;

	private BigDecimal percent;

	private ItemFamily familyLimit;

	private LocalDate startDate;
}
