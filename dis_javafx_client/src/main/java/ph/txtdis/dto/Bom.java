package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.UomType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Bom extends AbstractAuditedId<Long> {

	private Item part;

	private UomType uom;

	private BigDecimal qty;
}
