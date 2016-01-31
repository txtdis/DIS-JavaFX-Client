package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class EntityCreationTracked<PK> extends AbstractId<PK>implements CreationTracked {

	private String createdBy;

	private ZonedDateTime createdOn;
}