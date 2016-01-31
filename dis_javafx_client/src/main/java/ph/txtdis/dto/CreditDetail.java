package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.util.DateTimeUtils.compareStartDates;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreditDetail extends EntityDecisionNeeded<Long> implements Comparable<CreditDetail> {

	private int termInDays, gracePeriodInDays;

	private BigDecimal creditLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(CreditDetail cd) {
		return compareStartDates(this, cd);
	}
}
