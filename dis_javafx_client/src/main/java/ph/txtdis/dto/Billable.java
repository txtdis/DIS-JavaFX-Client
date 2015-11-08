package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Billable extends AbstractBookedOrder<Long> {

	private BigDecimal actualValue;

	private long numId;

	private String prefix, suffix;

	private boolean printed;

	public String getOrderNo() {
		return prefix() + numId() + suffix();
	}

	@Override
	public String toString() {
		return text() + " No. " + getOrderNo();
	}

	private Long numId() {
		return numId < 0 ? -numId : numId;
	}

	private String prefix() {
		return prefix == null ? "" : prefix + "-";
	}

	private String suffix() {
		return suffix == null ? "" : suffix;
	}

	private String text() {
		return numId < 0 ? "D/R" : "S/I";
	}
}
