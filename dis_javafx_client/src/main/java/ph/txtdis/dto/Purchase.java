package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Purchase extends AbstractAuditedId<Long> {

	private Customer customer;

	private LocalDate orderDate;

	private String remarks;

	private List<SoldDetail> details;

	protected User deactivatedBy;

	protected ZonedDateTime deactivatedOn;

	protected User mailedBy;

	protected ZonedDateTime mailedOn;

	protected User sentBy;

	protected ZonedDateTime sentOn;

	protected User receivedBy;

	protected ZonedDateTime receivedOn;

	@Override
	public String toString() {
		return "P/O No. " + getId();
	}
}
