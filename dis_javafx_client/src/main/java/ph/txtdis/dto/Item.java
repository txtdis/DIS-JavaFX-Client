package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ItemType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Item extends AbstractAuditedId<Long> {

	private String name;

	private String deactivatedBy;

	private ZonedDateTime deactivatedOn;

	private String description;

	private ItemType type;

	private ItemFamily family;

	private String vendorId;

	private boolean notDiscounted;

	private List<QtyPerUom> qtyPerUomList;

	private List<Price> priceList;

	private List<VolumeDiscount> volumeDiscounts;

	private List<Bom> boms;

	public VolumeDiscount getLatestVolumeDiscount(LocalDate date) {
		try {
			return getVolumeDiscounts().stream().filter(vd -> vd.getStartDate().compareTo(date) <= 0)
					.max(VolumeDiscount::compareTo).get();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
