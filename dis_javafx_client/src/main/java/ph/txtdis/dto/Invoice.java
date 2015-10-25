package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Invoice extends AbstractBookedOrder<Long> {

	private BigDecimal actualValue;

	private Long numId;

	private String prefix, suffix;

	public String getOrderNo() {
		return prefix() + numId + suffix();
	}

	@Override
	public String toString() {
		return "S/I No. " + getOrderNo();
	}

	private String prefix() {
		return prefix == null ? "" : prefix + "-";
	}

	private String suffix() {
		return suffix == null ? "" : suffix;
	}
}
