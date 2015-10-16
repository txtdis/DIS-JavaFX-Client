package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemittanceSettlement extends AbstractTrackedId<Long> {

	private Truck truck;

	private User reconciledBy;

	private ZonedDateTime reconciledOn;
}
