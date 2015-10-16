package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolumeDiscount extends AbstractTrackedId<Long>implements Comparable<VolumeDiscount> {

	private VolumeDiscountType type;

	private UomType uom;

	private int cutOff;

	private BigDecimal discount;

	private LocalDate startDate;

	private Channel channelLimit;

	@Override
	public int compareTo(VolumeDiscount o) {
		return getStartDate().compareTo(o.getStartDate());
	}
}
