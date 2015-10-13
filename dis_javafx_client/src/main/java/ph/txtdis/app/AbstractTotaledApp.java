package ph.txtdis.app;

import static ph.txtdis.type.Type.CURRENCY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.table.AppTable;
import ph.txtdis.service.Spreadsheet;

public abstract class AbstractTotaledApp<AT extends AppTable<T>, AS extends Spreadsheet<T>, T> extends AbstractExcelApp<AT, AS, T> {

	@Autowired
	private LabelFactory label;

	private Label subheader;

	private List<AppField<BigDecimal>> totalDisplays;

	@Override
	public void refresh() {
		try {
			refreshTablePane();
			setFocus();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	protected List<AppField<BigDecimal>> createTotalDisplays(int count) {
		totalDisplays = new ArrayList<>();
		for (int i = 0; i < count; i++)
			totalDisplays.add(new AppField<BigDecimal>().readOnly().build(CURRENCY));
		return totalDisplays;
	}

	@Override
	protected String headerText() {
		return service.getHeaderText();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return Arrays.asList(boxedTablePane());
	}

	protected void refreshTable() throws Exception {
		table.items(service.list());
	}

	@Override
	protected String titleText() {
		return service.getTitleText();
	}

	private HBox boxedTablePane() {
		VBox vbox = new VBox(getSubheaderPane(), table.build(), totalPane());
		return box.hpane(vbox);
	}

	private Label createSubheader() {
		return service.getSubheaderText() == null ? null : label.subheader(service.getSubheaderText());
	}

	private HBox createSubheaderPane() {
		HBox p = new HBox();
		p.setPadding(new Insets(20, 0, 0, 0));
		p.setAlignment(Pos.CENTER);
		return p;
	}

	private HBox getSubheaderPane() {
		HBox p = createSubheaderPane();
		subheader = createSubheader();
		if (subheader != null)
			p.getChildren().setAll(subheader);
		return p;
	}

	private void refreshSubheader() {
		if (subheader != null) {
			subheader.setText(service.getSubheaderText());
			table.setId(service.getSubheaderText());
		}
	}

	private void refreshTablePane() throws Exception {
		refreshSubheader();
		refreshTable();
		refreshTotals();
	}

	private void refreshTotals() {
		List<BigDecimal> list = service.getTotals();
		if (list != null)
			for (int i = 0; i < list.size(); i++)
				totalDisplays().get(i).setValue(service.getTotals().get(i));
		table.setUserData(list);
	}

	private List<AppField<BigDecimal>> totalDisplays() {
		return totalDisplays;
	}

	private HBox totalPane() {
		HBox box = new HBox();
		if (totalDisplays() != null)
			box.getChildren().setAll(totalDisplays());
		box.setPadding(new Insets(0, 20, 0, 0));
		box.setAlignment(Pos.CENTER_RIGHT);
		return box;
	}
}
