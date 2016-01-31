package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.util.DateTimeUtils.compareStartDates;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDiscount extends EntityDecisionNeeded<Long> implements Comparable<CustomerDiscount> {

	private int level;

	private BigDecimal percent;

	private ItemFamily familyLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(CustomerDiscount cd) {
		return compareStartDates(this, cd);
	}
}
