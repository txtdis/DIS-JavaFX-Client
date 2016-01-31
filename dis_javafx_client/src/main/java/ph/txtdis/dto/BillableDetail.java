package ph.txtdis.dto;

import static ph.txtdis.util.NumberUtils.isNegative;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Data
@EqualsAndHashCode(callSuper = true)
public class BillableDetail extends AbstractId<Long> {

	private String itemName;

	private UomType uom;

	private BigDecimal initialQty, returnedQty;

	private QualityType quality;

	private BigDecimal priceValue;

	private Integer onPurchaseDaysLevel, onReceiptDaysLevel;

	public BigDecimal getQty() {
		BigDecimal d = initialQty().subtract(returnedQty());
		return isNegative(d) ? ZERO : d;
	}

	public BigDecimal getSubtotalValue() {
		return price().multiply(getQty());
	}

	private BigDecimal initialQty() {
		return initialQty == null ? ZERO : initialQty;
	}

	private BigDecimal price() {
		return priceValue == null ? ZERO : priceValue;
	}

	private BigDecimal returnedQty() {
		return returnedQty == null ? ZERO : returnedQty;
	}
}
