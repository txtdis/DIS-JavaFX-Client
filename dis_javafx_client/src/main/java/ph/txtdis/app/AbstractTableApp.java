package ph.txtdis.app;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.scene.Node;
import ph.txtdis.fx.table.AppTable;
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
		String module = service.getModule();
		return StringUtils.capitalize(module);
	}

	@Override
	protected String getHeaderText() {
		return capitalizedModule() + " List";
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return Arrays.asList(box.forHorizontalPane(table.build()));
	}

	@Override
	protected String getTitleText() {
		return capitalizedModule() + " Master";
	}
}
