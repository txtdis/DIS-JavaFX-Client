package ph.txtdis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WeeklyVisit extends AbstractId<Long> {

	private int weekNo;

	private boolean sun, mon, tue, wed, thu, fri, sat;
}
