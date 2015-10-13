package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Remittance extends AbstractAuditedId<Long> {

	private LocalDate remitDate;

	private Customer bank;

	private String reference;

	private BigDecimal value;

	private User collector;

	private String remarks;

	private List<RemittanceDetail> details;

	@Override
	public String toString() {
		return "R/S No. " + getId();
	}
}
