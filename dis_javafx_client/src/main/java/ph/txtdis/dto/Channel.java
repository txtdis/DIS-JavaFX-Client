package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ph.txtdis.type.BillingType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Channel extends EntityCreationTracked<Long> {

	private BillingType type;

	private String name;

	private boolean visited;

	public Channel(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
