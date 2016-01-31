package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.CHECKBOX;
import static ph.txtdis.type.Type.INTEGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.WeeklyVisit;

@Lazy
@Component("visitScheduleTable")
public class VisitScheduleTable extends AppTable<WeeklyVisit> {

	@Autowired
	private Column<WeeklyVisit, Integer> weekNo;

	@Autowired
	private Column<WeeklyVisit, Boolean> sun, mon, tue, wed, thu, fri, sat;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				weekNo.ofType(INTEGER).build("Week No.", "weekNo"), //
				sun.ofType(CHECKBOX).build("Sun", "sun"), //
				mon.ofType(CHECKBOX).build("Mon", "mon"), //
				tue.ofType(CHECKBOX).build("Tue", "tue"), //
				wed.ofType(CHECKBOX).build("Wed", "wed"), //
				thu.ofType(CHECKBOX).build("Thu", "thu"), //
				fri.ofType(CHECKBOX).build("Fri", "fri"), //
				sat.ofType(CHECKBOX).build("Sat", "sat"));
	}
}
