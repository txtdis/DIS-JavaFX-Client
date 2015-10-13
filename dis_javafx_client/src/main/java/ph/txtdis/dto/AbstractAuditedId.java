package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditedId<PK> extends AbstractId<PK>implements Audited {

	private String createdBy;

	private ZonedDateTime createdOn;
}