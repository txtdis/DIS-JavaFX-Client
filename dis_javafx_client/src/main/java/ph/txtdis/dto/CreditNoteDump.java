package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CreditNoteDump implements Keyed<Long> {

	private BigDecimal balanceValue, paymentValue, totalValue;

	private LocalDate creditDate, paymentDate;

	private Long id;

	private String description, remarks, reference, paymentRemarks;
}
