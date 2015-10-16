package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemTree extends AbstractTrackedId<Long> {

	private ItemFamily family;

	private ItemFamily parent;
}
