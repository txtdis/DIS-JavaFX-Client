package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.type.ModuleType.INVOICE;

import lombok.Data;

@Data
public class Vat implements Keyed<Long>, Typed {

	private Long id, nbrId;

	private String prefix, suffix, customer;

	private LocalDate orderDate;

	private BigDecimal value, vatValue;

	@Override
	public String type() {
		return INVOICE.toString();
	}
}
