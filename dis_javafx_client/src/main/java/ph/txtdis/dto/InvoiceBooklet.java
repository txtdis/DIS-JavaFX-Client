package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceBooklet extends AbstractAuditedId<Long> {

	private String idPrefix, idSuffix;

	private Long startId, endId;

	private User issuedTo;

	@Override
	public String toString() {
		return "Booklet " + (idPrefix + startId + idSuffix) + " - " + (idPrefix + endId + idSuffix);
	}
}
