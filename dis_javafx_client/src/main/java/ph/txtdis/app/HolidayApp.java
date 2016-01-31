package ph.txtdis.app;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.Holiday;
import ph.txtdis.fx.table.HolidayTable;
import ph.txtdis.service.HolidayService;

@Lazy
@Component("holidayApp")
public class HolidayApp extends AbstractTableApp<HolidayTable, HolidayService, Holiday> {
}
