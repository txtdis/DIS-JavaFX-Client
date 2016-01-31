package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ItemTier;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemFamily extends EntityCreationTracked<Long> implements Comparable<ItemFamily> {

	private String name;

	private ItemTier tier;

	@Override
	public int compareTo(ItemFamily f) {
		return getTier().compareTo(f.getTier());
	}

	@Override
	public String toString() {
		return name;
	}
}
