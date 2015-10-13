package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDiscount extends AbstractAuditedId<Long>implements StartDated, Comparable<CustomerDiscount> {

	private Integer level;

	private BigDecimal percent;

	private ItemFamily familyLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(CustomerDiscount o) {
		return getStartDate().compareTo(o.getStartDate());
	}
}
