package ph.txtdis.dto;

import static ph.txtdis.util.NumberUtils.toBigDecimal;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.math.BigDecimal.ZERO;

import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.INVOICE;

import lombok.Data;

@Data
public class CustomerReceivable implements Keyed<Long>, Typed {

	private Long id;

	private String orderNo;

	private LocalDate orderDate;

	private LocalDate dueDate;

	private int daysOverCount;

	private BigDecimal unpaidValue;

	private BigDecimal totalValue;

	public String getOrderNo() {
		return isNegative() ? orderNo.replace("-", "(") + ")" : orderNo;
	}

	@Override
	public String type() {
		return (isNegative() ? DELIVERY_REPORT : INVOICE).toString();
	}

	private boolean isNegative() {
		try {
			return toBigDecimal(orderNo).compareTo(ZERO) < 0;
		} catch (Exception e) {
			return false;
		}
	}
}