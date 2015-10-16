package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Price extends AbstractTrackedId<Long>implements Comparable<Price> {

	private PricingType type;

	private BigDecimal priceValue;

	private LocalDate startDate;

	private Channel channelLimit;

	@Override
	public int compareTo(Price price) {
		return getStartDate().compareTo(price.getStartDate());
	}
}
