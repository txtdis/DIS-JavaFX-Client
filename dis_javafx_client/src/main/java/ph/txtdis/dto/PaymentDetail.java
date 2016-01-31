package ph.txtdis.dto;

import static ph.txtdis.util.NumberUtils.isNegative;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.INVOICE;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDetail extends AbstractId<Long> implements Typed {

	private String orderNo, customerName;

	private LocalDate dueDate;

	private BigDecimal totalDueValue, paymentValue;

	@Override
	public String type() {
		return (isNegative(orderNo) ? DELIVERY_REPORT : INVOICE).toString();
	}
}
