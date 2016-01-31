package ph.txtdis.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.util.TypeMap;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditedId<PK> extends EntityCreationTracked<PK> {

	private Boolean isValid;

	private String auditedBy;

	private ZonedDateTime auditedOn;

	public String getValidity() {
		return isValid == null ? "" : new TypeMap().icon(isValid ? "accept" : "reject");
	}
}