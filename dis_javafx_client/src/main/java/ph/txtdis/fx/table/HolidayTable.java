package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Holiday;
import ph.txtdis.fx.dialog.HolidayDialog;

@Scope("prototype")
@Component("holidayTable")
public class HolidayTable extends NameListTable<Holiday, HolidayDialog> {

	@Autowired
	private Column<Holiday, LocalDate> date;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				date.ofType(DATE).build("Date", "declaredDate"), //
				name.ofType(TEXT).width(180).build("Name", "name"), //
				createdBy.ofType(TEXT).width(100).build("Created by", "createdBy"), //
				createdOn.ofType(TIMESTAMP).build("Created on", "createdOn"));
	}
}
