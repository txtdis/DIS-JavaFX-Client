package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockTakeDetail extends AbstractId<Long> {

	private String name;

	private UomType uom;

	private BigDecimal qty;

	private QualityType quality;
}
