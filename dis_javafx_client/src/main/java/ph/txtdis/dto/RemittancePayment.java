package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class RemittancePayment {

	private Long remitId;

	private LocalDate remitDate;

	private BigDecimal value;
}
