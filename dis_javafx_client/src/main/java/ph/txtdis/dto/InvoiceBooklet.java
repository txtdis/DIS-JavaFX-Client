package ph.txtdis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceBooklet extends EntityCreationTracked<Long> {

	private String prefix, suffix, issuedTo;

	private Long startId, endId;

	public String getPrefix() {
		return prefix == null ? "" : prefix;
	}

	public String getSuffix() {
		return suffix == null ? "" : suffix;
	}

	@Override
	public String toString() {
		return "Booklet " + (prefix() + startId + getSuffix()) + " - " + (prefix() + endId + getSuffix());
	}

	private String prefix() {
		return prefix == null ? "" : prefix + "-";
	}
}
