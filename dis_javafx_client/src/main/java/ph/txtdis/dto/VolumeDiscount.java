package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ph.txtdis.util.DateTimeUtils.compareStartDates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VolumeDiscount extends EntityDecisionNeeded<Long> implements Comparable<VolumeDiscount>, StartDated {

	private VolumeDiscountType type;

	private UomType uom;

	private int cutoff;

	private BigDecimal discount;

	private Channel channelLimit;

	private LocalDate startDate;

	@Override
	public int compareTo(VolumeDiscount vd) {
		return compareStartDates(this, vd);
	}
}
