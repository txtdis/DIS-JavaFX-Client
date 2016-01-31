package ph.txtdis.fx.table;

import static ph.txtdis.type.Type.ID;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import ph.txtdis.dto.Keyed;
import ph.txtdis.fx.dialog.Inputted;

public abstract class NameListTable<S extends Keyed<?>, D extends Inputted<S>> extends AppTable<S> {

	@Autowired
	private AppendContextMenu<S> append;

	@Autowired
	protected Column<S, Long> id;

	@Autowired
	protected Column<S, String> name;

	@Autowired
	protected Column<S, String> createdBy;

	@Autowired
	protected Column<S, ZonedDateTime> createdOn;

	@Autowired
	private D dialog;

	@Override
	@SuppressWarnings("unchecked")
	protected void addColumns() {
		getColumns().setAll(//
				id.ofType(ID).build("ID No.", "id"), //
				name.ofType(TEXT).width(180).build("Name", "name"), //
				createdBy.ofType(TEXT).width(100).build("Created by", "createdBy"), //
				createdOn.ofType(TIMESTAMP).build("Created on", "createdOn"));
	}

	@Override
	protected void addProperties() {
		append.addMenu(this, dialog);
	}
}
