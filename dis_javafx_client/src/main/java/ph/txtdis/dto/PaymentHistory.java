package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class PaymentHistory implements Keyed<Long> {

	private Long id;

	private LocalDate paymentDate;

	private BigDecimal value;

	private Boolean depositBool, valid;
}