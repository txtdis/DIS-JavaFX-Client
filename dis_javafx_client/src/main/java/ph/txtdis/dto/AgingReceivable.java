package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AgingReceivable implements Keyed<Long> {

	private Long id;

	private String seller, customer;

	private BigDecimal currentValue, oneToSevenValue, eightToFifteenValue, sixteenToThirtyValue, greaterThanThirtyValue,
			agingValue, totalValue;
}