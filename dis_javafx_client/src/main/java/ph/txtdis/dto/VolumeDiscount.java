package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.UomType;
import ph.txtdis.type.VolumeDiscountType;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolumeDiscount extends AbstractTrackedId<Long> {

	private VolumeDiscountType type;

	private UomType uom;

	private int cutoff;

	private BigDecimal discount;

	private LocalDate startDate;

	private Channel channelLimit;
}
