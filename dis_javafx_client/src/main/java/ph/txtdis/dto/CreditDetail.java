package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreditDetail extends AbstractTrackedId<Long> implements StartDated {

	private int termInDays, gracePeriodInDays;

	private BigDecimal creditLimit;

	private LocalDate startDate;
}
