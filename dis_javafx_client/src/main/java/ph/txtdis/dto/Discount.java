package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Discount extends AbstractId<Long> {

	private int level;

	private BigDecimal percent;
}
