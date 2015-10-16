package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreditDetail extends AbstractId<Long> implements Comparable<CreditDetail>, StartDated {

	private int termInDays, gracePeriodInDays;

	private BigDecimal creditLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(CreditDetail o) {
		return getStartDate().compareTo(o.getStartDate());
	}
}
