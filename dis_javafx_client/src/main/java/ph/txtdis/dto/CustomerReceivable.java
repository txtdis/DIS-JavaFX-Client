package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerReceivable implements Keyed<Long> {

	private Long id;

	private String orderNo;

	private LocalDate orderDate;

	private LocalDate dueDate;

	private int daysOverCount;

	private BigDecimal unpaidValue;

	private BigDecimal totalValue;
}