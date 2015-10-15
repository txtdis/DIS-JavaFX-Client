package ph.txtdis.app;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import ph.txtdis.dto.Customer;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.dialog.SearchDialog;
import ph.txtdis.fx.tab.CreditTab;
import ph.txtdis.fx.tab.CustomerDiscountTab;
import ph.txtdis.fx.tab.CustomerTab;
import ph.txtdis.fx.tab.InputTab;
import ph.txtdis.service.CustomerService;

@Lazy
@Component("customerApp")
public class CustomerApp extends AbstractIdApp<Customer, CustomerService, Long> {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppButton searchButton;

	@Autowired
	private AppButton deactivateButton;

	@Autowired
	private CustomerTab customerTab;

	@Autowired
	private CreditTab creditTab;

	@Autowired
	private CustomerDiscountTab discountTab;

	@Autowired
	private AppField<String> deactivatedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> deactivatedOnDisplay;

	@Autowired
	private SearchDialog searchDialog;

	@Autowired
	private CustomerListApp customerListApp;

	private List<InputTab> inputTabs;

	public void listSearchResults() {
		customerListApp.addParent(this).start();
		service.set(customerListApp.getSelection());
	}

	@Override
	public void refresh() {
		inputTabs.forEach(t -> t.refresh());
		refreshDeactivationNodes();
		super.refresh();
	}

	@Override
	public void save() throws Exception {
		inputTabs.forEach(t -> t.save());
		service.save();
	}

	@Override
	public void setFocus() {
		customerTab.select();
	}

	// @formatter:off
	private List<Node> deactivationNodes() {
		return Arrays.asList(
				label.name("Deactivated by"), deactivatedByDisplay.readOnly().width(120).build(TEXT),
				label.name("on"), deactivatedOnDisplay.readOnly().build(TIMESTAMP));
	}
	// @formatter:on

	private List<InputTab> inputTabs() {
		inputTabs = Arrays.asList(customerTab.build(), creditTab.build(), discountTab.build());
		setBindings();
		return inputTabs;
	}

	private BooleanBinding isAlreadyDeactivated() {
		return deactivatedByDisplay.isNotEmpty();
	}

	private void openSearchDialog() {
		searchDialog.criteria("name").start();
		String name = searchDialog.getText();
		if (name != null)
			trySearching(name);
	}

	private void refreshDeactivationNodes() {
		deactivatedByDisplay.setValue(service.getDeactivatedBy());
		deactivatedOnDisplay.setValue(service.getDeactivatedOn());
	}

	private void search(String name) throws Exception {
		service.search(name);
		listSearchResults();
		refresh();
		setFocus();
	}

	private void setBindings() {
		creditTab.disableIf(customerTab.showsCustomerIsNotAnOutlet());
		discountTab.disableIf(creditTab.isDisabledNow());
		deactivateButton.disableIf(customerTab.showsANewCustomer().or(isAlreadyDeactivated()));
		saveButton.disableIf(customerTab.showsNoSelectedCustomerType().or(isAlreadyDeactivated())
				.or(customerTab.showsCustomerIsAnOutlet().and(customerTab.showsNoDesignatedRoutes())));
	}

	private TabPane tabPane() {
		TabPane tabPane = new TabPane();
		tabPane.setStyle("-fx-tab-min-width: 80;");
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.getTabs().addAll(tabs());
		setBindings();
		return tabPane;
	}

	private List<Tab> tabs() {
		return inputTabs().stream().map(t -> t.asTab()).collect(Collectors.toList());
	}

	private void tryDeactivating() {
		try {
			service.deactivate();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	private void trySearching(String name) {
		try {
			search(name);
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	protected List<AppButton> addButtons() {
		List<AppButton> list = new ArrayList<>(super.addButtons());
		list.addAll(Arrays.asList(searchButton, deactivateButton));
		return list;
	}

	@Override
	protected HBox auditPane() {
		HBox hbox = super.auditPane();
		hbox.getChildren().addAll(deactivationNodes());
		return hbox;
	}

	@Override
	protected void createButtons() {
		searchButton.icon("search").tooltip("Search...").build();
		deactivateButton.icon("deactivate").tooltip("Next...").build();
		super.createButtons();
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		return Arrays.asList(tabPane());
	}

	@Override
	protected void setActionOnButtonClick() {
		searchButton.setOnAction(e -> openSearchDialog());
		deactivateButton.setOnAction(e -> tryDeactivating());
		super.setActionOnButtonClick();
	}
}
