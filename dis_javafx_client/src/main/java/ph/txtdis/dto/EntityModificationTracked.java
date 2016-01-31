package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class EntityModificationTracked<PK> extends EntityCreationTracked<PK> implements ModificationTracked {

	private String deactivatedBy, lastModifiedBy;

	private ZonedDateTime deactivatedOn, lastModifiedOn;
}