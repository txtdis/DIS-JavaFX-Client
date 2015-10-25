package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Discount extends AbstractTrackedId<Long>implements StartDated, Comparable<Discount> {

	private Integer level;

	private BigDecimal percent;

	private ItemFamily familyLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(Discount o) {
		return getStartDate().compareTo(o.getStartDate());
	}
}
