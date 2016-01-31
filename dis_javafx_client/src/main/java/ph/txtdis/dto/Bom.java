package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Bom extends EntityCreationTracked<Long> {

	private Item part;

	private BigDecimal qty;
}
