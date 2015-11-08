package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditedId<PK> extends AbstractTrackedId<PK> {

	private Boolean isValid;

	private String auditedBy;

	private ZonedDateTime auditedOn;
}