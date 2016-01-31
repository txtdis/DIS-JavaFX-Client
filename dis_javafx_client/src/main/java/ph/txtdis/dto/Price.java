package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.util.DateTimeUtils.compareStartDates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Price extends EntityDecisionNeeded<Long> implements Comparable<Price> {

	private PricingType type;

	private BigDecimal priceValue;

	private Channel channelLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(Price p) {
		return compareStartDates(this, p);
	}
}
