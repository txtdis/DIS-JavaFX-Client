package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Inventory implements Keyed<Long> {

	private Long id;

	private String item;

	private Integer daysLevel;

	private BigDecimal avgDailySoldQty, goodQty, badQty, value, obsolesenceValue;
}
