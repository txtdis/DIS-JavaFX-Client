package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetail extends AbstractId<Long> {

	private Item item;

	private UomType uom;

	private BigDecimal qty;

	private QualityType quality;

	public Long getItemId() {
		return getItem() == null ? null : getItem().getId();
	}

	public String getItemName() {
		return getItem() == null ? null : getItem().getName();
	}
}
