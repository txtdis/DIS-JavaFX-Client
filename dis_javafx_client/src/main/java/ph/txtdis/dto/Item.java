package ph.txtdis.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ItemType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Item extends EntityModificationTracked<Long> {

	private boolean notDiscounted;

	private String description, name, vendorId;

	private ItemFamily family;

	private ItemType type;

	private List<Bom> boms;

	private List<Price> priceList;

	private List<QtyPerUom> qtyPerUomList;

	private List<VolumeDiscount> volumeDiscounts;

	private LocalDate endOfLife;

	public VolumeDiscount getLatestVolumeDiscount(LocalDate date) {
		try {
			return getVolumeDiscounts().stream().filter(vd -> vd.getStartDate().compareTo(date) <= 0)
					.max((a, b) -> a.getStartDate().compareTo(b.getStartDate())).get();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
