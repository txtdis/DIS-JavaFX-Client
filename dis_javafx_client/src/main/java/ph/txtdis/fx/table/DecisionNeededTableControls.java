package ph.txtdis.fx.table;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.BOOLEAN;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.control.TableView;
import ph.txtdis.dto.Keyed;
import ph.txtdis.fx.dialog.Inputted;

@Scope("prototype")
@Component("decisionNeededTable")
public class DecisionNeededTableControls<S extends Keyed<?>> {

	@Autowired
	private AppendContextMenu<S> append;

	@Autowired
	private Column<S, LocalDate> startDate;

	@Autowired
	private Column<S, String> givenBy;

	@Autowired
	private Column<S, ZonedDateTime> givenOn;

	@Autowired
	private Column<S, Boolean> approved;

	@Autowired
	private Column<S, String> decidedBy;

	@Autowired
	private Column<S, ZonedDateTime> decidedOn;

	@Autowired
	private Column<S, String> remarks;

	public List<Column<S, ?>> addColumns() {
		return asList(startDate.ofType(DATE).build("Start\nDate", "startDate"),
				givenBy.ofType(TEXT).width(100).build("Given\nby", "createdBy"),
				givenOn.ofType(TIMESTAMP).build("Given\non", "createdOn"),
				approved.ofType(BOOLEAN).build("OK'd", "isValid"),
				decidedBy.ofType(TEXT).width(120).build("Dis/approved\nby", "decidedBy"),
				decidedOn.ofType(TIMESTAMP).build("Dis/approved\non", "decidedOn"),
				remarks.ofType(TEXT).width(320).build("Remarks", "remarks"));
	}

	public AppendContextMenu<S> addContextMenu(TableView<S> t, Inputted<S> dialog) {
		return append.addMenu(t, dialog);
	}
}
