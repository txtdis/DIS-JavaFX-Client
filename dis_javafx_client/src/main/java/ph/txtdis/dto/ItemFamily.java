package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ItemTier;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemFamily extends AbstractTrackedId<Long> {

	private String name;

	private ItemTier tier;

	@Override
	public String toString() {
		return name;
	}
}
