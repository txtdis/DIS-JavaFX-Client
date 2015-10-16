package ph.txtdis.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemittanceSettlementAdjustment extends AbstractTrackedId<Long> {

	private LocalDate pickDate;

	private Truck truck;

	private Invoice invoice;

	private String actionTaken;
}
