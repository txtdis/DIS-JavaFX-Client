package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Invoice extends AbstractBookedOrder<String> {

	private BigDecimal actualValue;

	private Long idNo;

	private String idPrefix, idSuffix;

	@Override
	public String getId() {
		return getIdPrefix() + idNo + getIdSuffix();
	}

	public String getIdPrefix() {
		return idPrefix == null ? "" : idPrefix;
	}

	public String getIdSuffix() {
		return idSuffix == null ? "" : idSuffix;
	}

	@Override
	public String toString() {
		return "S/I No. " + getId();
	}
}
