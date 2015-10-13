package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DailyStockTakeSummary extends AbstractId<LocalDate> {

	private User cutoffBy;

	private ZonedDateTime cutoffOn;

	private User closedBy;

	private ZonedDateTime closedOn;

	private User reconciledBy;

	private ZonedDateTime reconciledOn;

	private User mailedBy;

	private ZonedDateTime mailedOn;

	private User approvedBy;

	private ZonedDateTime approvedOn;

	private boolean isApproved;

	private User completedBy;

	private ZonedDateTime completedOn;
}
