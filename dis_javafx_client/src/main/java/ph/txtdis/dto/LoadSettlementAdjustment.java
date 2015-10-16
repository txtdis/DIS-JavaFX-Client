package ph.txtdis.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoadSettlementAdjustment extends AbstractTrackedId<Long> {

	private LocalDate pickDate;

	private Truck truck;

	private Item item;

	private String actionTaken;
}
