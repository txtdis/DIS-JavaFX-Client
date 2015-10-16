package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditedId<PK> extends AbstractTrackedId<PK> {

	private String revisedBy;

	private ZonedDateTime revisedOn;

	private String invalidatedBy;

	private ZonedDateTime invalidatedOn;

	private String validatedBy;

	private ZonedDateTime validatedOn;
}