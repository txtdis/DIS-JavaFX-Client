package ph.txtdis.dto;

import static ph.txtdis.type.ModuleType.SALES_ORDER;

import lombok.Data;

@Data
public class Booking implements Keyed<Long>, Typed {

	private Long id;

	private String customer, location, route;

	@Override
	public String type() {
		return SALES_ORDER.toString();
	}
}
