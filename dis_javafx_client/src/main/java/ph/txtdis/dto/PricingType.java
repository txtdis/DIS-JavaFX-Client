package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PricingType extends AbstractAuditedId<Long> {

	private String name;

	@Override
	public String toString() {
		return name;
	}
}
