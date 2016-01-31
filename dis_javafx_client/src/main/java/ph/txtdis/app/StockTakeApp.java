package ph.txtdis.app;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.DATE;
import static ph.txtdis.type.Type.TEXT;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import ph.txtdis.dto.StockTake;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.fx.table.StockTakeTable;
import ph.txtdis.service.StockTakeService;

@Lazy
@Component("stockTakeApp")
public class StockTakeApp extends AbstractIdApp<StockTakeService, Long, Long> {

	@Autowired
	private AppCombo<String> warehouseCombo;

	@Autowired
	private AppCombo<String> checkerCombo;

	@Autowired
	private AppCombo<String> takerCombo;

	@Autowired
	private AppGridPane gridPane;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<LocalDate> countDateDisplay;

	@Autowired
	private StockTakeTable table;

	private BooleanProperty isUserAnAuditor, isCountToday;

	@Override
	public void refresh() {
		countDateDisplay.setValue(service.getCountDate());
		warehouseCombo.items(service.listWarehouses());
		checkerCombo.items(service.listCheckers());
		takerCombo.items(service.listTakers());
		table.items(stockTake().getDetails());
		super.refresh();
	}

	@Override
	public void setFocus() {
		warehouseCombo.requestFocus();
	}

	private AppGridPane gridPane() {
		gridPane.getChildren().clear();
		gridPane.add(label.field("Date"), 0, 0);
		gridPane.add(countDateDisplay.readOnly().build(DATE), 1, 0, 2, 1);
		gridPane.add(label.field("Warehouse"), 3, 0);
		gridPane.add(warehouseCombo, 4, 0);
		gridPane.add(label.field("Checker"), 5, 0);
		gridPane.add(checkerCombo, 6, 0);
		gridPane.add(label.field("Taker"), 7, 0);
		gridPane.add(takerCombo, 8, 0);
		gridPane.add(label.field("Remarks"), 0, 1);
		gridPane.add(remarksDisplay.readOnly().build(TEXT), 1, 1, 10, 1);
		return gridPane;
	}

	private StockTake stockTake() {
		return service.get();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(gridPane(), box.forHorizontalPane(table.build()), trackedPane());
	}

	@Override
	protected void setBindings() {
		isUserAnAuditor = new SimpleBooleanProperty(service.isUserAnAuditor());
		isCountToday = new SimpleBooleanProperty(service.isCountToday());
		saveButton.disableIf(table.isEmpty()//
				.or(isUserAnAuditor.not()) //
				.or(isCountToday.not()));
		checkerCombo.disableIf(warehouseCombo.isEmpty());
		takerCombo.disableIf(checkerCombo.isEmpty());
		table.disableIf(takerCombo.isEmpty());
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		warehouseCombo.setOnAction(e -> stockTake().setWarehouse(warehouseCombo.getValue()));
		checkerCombo.setOnAction(e -> stockTake().setChecker(checkerCombo.getValue()));
		takerCombo.setOnAction(e -> stockTake().setTaker(takerCombo.getValue()));
		table.setOnItemChange(i -> stockTake().setDetails(table.getItems()));
	}
}
