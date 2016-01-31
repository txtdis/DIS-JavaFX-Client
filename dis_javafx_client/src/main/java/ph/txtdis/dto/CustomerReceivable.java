package ph.txtdis.dto;

import static ph.txtdis.util.NumberUtils.isNegative;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.INVOICE;

import lombok.Data;

@Data
public class CustomerReceivable implements Keyed<Long>, Typed {

	private Long id;

	private String orderNo;

	private LocalDate orderDate, dueDate;

	private BigDecimal unpaidValue, totalValue;

	private int daysOverCount;

	@Override
	public String type() {
		return (isNegative(orderNo) ? DELIVERY_REPORT : INVOICE).toString();
	}
}