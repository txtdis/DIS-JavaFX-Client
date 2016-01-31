package ph.txtdis.app;

import static ph.txtdis.type.Type.CURRENCY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.AppBoxPaneFactory;
import ph.txtdis.fx.table.AppTable;
import ph.txtdis.service.TotaledTable;

@Component("totaledTableApp")
public class TotaledTableApp {

	@Autowired
	private AppBoxPaneFactory box;

	@Autowired
	private LabelFactory label;

	private AppTable<?> table;

	private Label subhead;

	private List<AppField<BigDecimal>> totalDisplays;

	public VBox addTablePane(AppTable<?> t) {
		table = t;
		VBox v = box.forVerticals(subheadPane(), t.build(), totalPane());
		HBox h = box.forHorizontalPane(v);
		return box.forVerticals(h);
	}

	public HBox addTotalDisplayPane(int count) {
		totalDisplays = new ArrayList<>();
		for (int i = 0; i < count; i++)
			totalDisplays.add(new AppField<BigDecimal>().readOnly().build(CURRENCY));
		return box.forTableTotals(totalDisplays);
	}

	public void refresh(TotaledTable s) throws Exception {
		refreshSubheader(s);
		refreshTotals(s);
	}

	private void refreshSubheader(TotaledTable s) {
		subhead.setText(s.getSubhead());
		table.setId(s.getSubhead());
	}

	private void refreshTotals(TotaledTable s) {
		List<BigDecimal> list = s.getTotals();
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++)
			totalDisplays.get(i).setValue(list.get(i));
	}

	private HBox subheadPane() {
		subhead = label.subheader("");
		return box.forSubheader(subhead);
	}

	private Node totalPane() {
		return box.forTableTotals(totalDisplays);
	}
}
