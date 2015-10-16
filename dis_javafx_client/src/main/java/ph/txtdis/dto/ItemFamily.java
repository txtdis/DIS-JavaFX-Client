package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ItemTier;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemFamily extends AbstractTrackedId<Long>implements Comparable<ItemFamily> {

	private String name;

	private ItemTier tier;

	@Override
	public int compareTo(ItemFamily o) {
		return getOrdinal(this).compareTo(getOrdinal(o));
	}

	@Override
	public String toString() {
		return name;
	}

	private Integer getOrdinal(ItemFamily o) {
		return Integer.valueOf(o.getTier().ordinal());
	}
}
