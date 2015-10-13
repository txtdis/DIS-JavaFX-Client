package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Vat implements Keyed<Long> {

	private Long id;

	private String customer;

	private LocalDate orderDate;

	private BigDecimal value;

	private BigDecimal vatValue;
}
