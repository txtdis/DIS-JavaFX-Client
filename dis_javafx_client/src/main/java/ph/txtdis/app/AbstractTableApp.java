package ph.txtdis.app;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.commons.lang3.StringUtils.capitalize;

import javafx.scene.Node;
import ph.txtdis.fx.table.AppTable;
import ph.txtdis.service.Iconed;
import ph.txtdis.service.Listed;

public abstract class AbstractTableApp<AT extends AppTable<T>, AS extends Listed<?>, T> extends AbstractApp {

	@Autowired
	protected AT table;

	@Autowired
	protected AS service;

	@Override
	public void refresh() {
		try {
			table.items(service.list());
			super.refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void setFocus() {
		table.requestFocus();
	}

	private String capitalizedModule() {
		return capitalize(service.getModule());
	}

	@Override
	protected String getFontIcon() {
		return ((Iconed) service).getFontIcon();
	}

	@Override
	protected String getHeaderText() {
		return capitalizedModule() + " List";
	}

	@Override
	protected String getTitleText() {
		return capitalizedModule() + " Master";
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(box.forHorizontalPane(table.build()));
	}
}
