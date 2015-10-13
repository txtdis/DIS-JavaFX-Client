package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Delivery extends AbstractBookedOrder<Long> {

	@Override
	public String toString() {
		return "D/R No. " + getId();
	}
}
