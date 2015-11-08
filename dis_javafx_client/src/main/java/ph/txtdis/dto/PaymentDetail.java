package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentDetail extends AbstractId<Long> {

	private String orderNo, customerName;

	private LocalDate dueDate;

	private BigDecimal totalDueValue, paymentValue;
}
