package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.UomType;

@Data
@EqualsAndHashCode(callSuper = true)
public class QtyPerUom extends EntityCreationTracked<Long> {

	private UomType uom;

	private BigDecimal qty;

	private Boolean purchased, sold, reported;
}
