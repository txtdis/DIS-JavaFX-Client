package ph.txtdis.app;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.Item;
import ph.txtdis.exception.DeactivatedException;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.NoVendorIdPurchasedItemException;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.dialog.SearchDialog;
import ph.txtdis.fx.tab.InputTab;
import ph.txtdis.fx.tab.ItemTab;
import ph.txtdis.fx.tab.PricingTab;
import ph.txtdis.fx.tab.VolumeDiscountTab;
import ph.txtdis.info.Information;
import ph.txtdis.service.ItemService;

@Scope("prototype")
@Component("itemApp")
public class ItemApp extends AbstractIdApp<ItemService, Long, Long> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppButton searchButton;

	@Autowired
	private AppButton deactivateButton;

	@Autowired
	private ItemTab itemTab;

	@Autowired
	private PricingTab pricingTab;

	@Autowired
	private VolumeDiscountTab discountTab;

	@Autowired
	private AppField<String> deactivatedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> deactivatedOnDisplay;

	@Autowired
	private AppField<String> lastModifiedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> lastModifiedOnDisplay;

	@Autowired
	private SearchDialog searchDialog;

	@Autowired
	private ItemListApp itemListApp;

	private List<InputTab> inputTabs;

	private BooleanProperty noChangesNeedingApproval;

	public void listSearchResults()
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			NotFoundException, DeactivatedException, NoVendorIdPurchasedItemException, RestException {
		itemListApp.addParent(this).start();
		Item c = itemListApp.getSelection();
		if (c != null)
			service.open(c.getId());
	}

	@Override
	public void refresh() {
		inputTabs.forEach(t -> t.refresh());
		refreshDeactivationNodes();
		refreshLastModificationNodes();
		super.refresh();
	}

	@Override
	public void save() {
		inputTabs.forEach(t -> t.save());
		super.save();
	}

	@Override
	public void setFocus() {
		itemTab.select();
	}

	private void checkForChangesNeedingApproval(Tab tab) {
		if (tab.isSelected()) {
			boolean b = service.noChangesNeedingApproval(tab.getText());
			noChangesNeedingApproval.set(b);
		}
	}

	private void deactivate() {
		try {
			service.deactivate();
		} catch (Exception e) {
			showErrorDialog(e);
		} catch (Information i) {
			dialog.show(i).addParent(this).start();
		} finally {
			refresh();
		}
	}

	private List<Node> deactivationNodes() {
		return Arrays.asList(//
				label.name("Deactivated by"), deactivatedByDisplay.readOnly().width(120).build(TEXT), //
				label.name("on"), deactivatedOnDisplay.readOnly().build(TIMESTAMP));
	}

	private List<InputTab> inputTabs() {
		return inputTabs = asList(itemTab.build(), pricingTab.build(), discountTab.build());
	}

	private BooleanBinding isAlreadyDeactivated() {
		return deactivatedByDisplay.isNotEmpty();
	}

	private List<Node> lastModificationNodes() {
		return Arrays.asList(//
				label.name("Last Modified by"), lastModifiedByDisplay.readOnly().width(120).build(TEXT), //
				label.name("on"), lastModifiedOnDisplay.readOnly().build(TIMESTAMP));
	}

	private void openSearchDialog() {
		searchDialog.criteria("name").start();
		String name = searchDialog.getText();
		if (name != null)
			search(name);
	}

	private void refreshDeactivationNodes() {
		deactivatedByDisplay.setValue(service.getDeactivatedBy());
		deactivatedOnDisplay.setValue(service.getDeactivatedOn());
	}

	private void refreshLastModificationNodes() {
		lastModifiedByDisplay.setValue(service.get().getLastModifiedBy());
		lastModifiedOnDisplay.setValue(service.get().getLastModifiedOn());
	}

	private void search(String name) {
		try {
			service.search(name);
			listSearchResults();
			refresh();
		} catch (Exception e) {
			showErrorDialog(e);
		}
	}

	private void showDecisionDialogToValidateOrder() {
		decisionNeededApp.showDecisionDialogForValidation(this, service);
		refresh();
	}

	private TabPane tabPane() {
		TabPane tabPane = new TabPane();
		tabPane.setStyle("-fx-tab-min-width: 80;");
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.getTabs().addAll(tabs());
		return tabPane;
	}

	private List<Tab> tabs() {
		return inputTabs().stream().map(t -> t.asTab()).collect(toList());
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> l = new ArrayList<>(super.addButtons());
		l.add(searchButton.icon("search").tooltip("Search...").build());
		l.add(deactivateButton.icon("deactivate").tooltip("Next...").build());
		l.add(decisionButton = decisionNeededApp.addDecisionButton());
		return l;
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return asList(tabPane(), trackedPane());
	}

	@Override
	protected void setBindings() {
		noChangesNeedingApproval = new SimpleBooleanProperty(true);
		pricingTab.disableIf(itemTab.hasIncompleteData());
		discountTab.disableIf(pricingTab.disabledProperty());
		decisionButton.disableIf(notPosted()//
				.or(noChangesNeedingApproval));
		deactivateButton.disableIf(notPosted()//
				.or(isAlreadyDeactivated()));
		saveButton.disableIf(isAlreadyDeactivated()//
				.or(itemTab.needsPrice().and(pricingTab.hasNoPrices()))//
				.or(itemTab.hasIncompleteData()));
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		searchButton.setOnAction(e -> openSearchDialog());
		deactivateButton.setOnAction(e -> deactivate());
		itemTab.setOnSelectionChanged(e -> noChangesNeedingApproval.set(true));
		pricingTab.setOnSelectionChanged(e -> checkForChangesNeedingApproval(pricingTab));
		discountTab.setOnSelectionChanged(e -> checkForChangesNeedingApproval(discountTab));
		decisionNeededApp.setDecisionButtonOnAction(e -> showDecisionDialogToValidateOrder());
	}

	@Override
	protected HBox trackedPane() {
		List<Node> l = new ArrayList<>(super.trackedPane().getChildren());
		l.addAll(deactivationNodes());
		l.addAll(lastModificationNodes());
		return box.forHorizontalPane(l);
	}
}
