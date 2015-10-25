package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Remittance extends AbstractTrackedId<Long> {

	private BigDecimal value;

	private List<RemittanceDetail> details;

	private LocalDate dueDate, depositDate;

	private LocalTime depositTime;

	private Long checkId, depositId, receiptId, transferId;

	private String draweeBank, depositBank, lastModifiedBy, payor, receivedBy, remarks;

	private ZonedDateTime lastModifiedOn, receivedOn;

	@Override
	public String toString() {
		return "Collection No. " + getId();
	}
}
