package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class EntityDecisionNeeded<PK> extends EntityCreationTracked<PK> {

	private Boolean isValid;

	private String remarks, decidedBy;

	private ZonedDateTime decidedOn;
}