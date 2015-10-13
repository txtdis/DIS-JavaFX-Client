package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemTree extends AbstractAuditedId<Long> {

	private ItemFamily family;

	private ItemFamily parent;
}
