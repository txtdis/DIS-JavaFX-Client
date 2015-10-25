package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SoldOrderDetail extends OrderDetail {

	private BigDecimal priceValue;

	public BigDecimal getSubtotalValue() {
		return getPriceValue() == null || getQty() == null ? null : getPriceValue().multiply(getQty());
	}
}
