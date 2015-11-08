package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTrackedId<PK> extends AbstractId<PK>implements Tracked {

	private String createdBy;

	private ZonedDateTime createdOn;
}