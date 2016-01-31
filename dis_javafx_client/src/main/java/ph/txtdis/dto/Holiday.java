package ph.txtdis.dto;

import java.time.LocalDate;

import static ph.txtdis.util.DateTimeUtils.toDateDisplay;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Holiday extends EntityCreationTracked<Long> {

	private LocalDate declaredDate;

	private String name;

	@Override
	public String toString() {
		return toDateDisplay(declaredDate) + " - " + name;
	}
}
