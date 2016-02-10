package ph.txtdis.fx.dialog;

import static ph.txtdis.util.SpringUtil.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static ph.txtdis.type.ModuleType.BAD_ORDER;
import static ph.txtdis.type.ModuleType.DELIVERY_REPORT;
import static ph.txtdis.type.ModuleType.DOWNLOAD;
import static ph.txtdis.type.ModuleType.INVOICE;
import static ph.txtdis.type.ModuleType.PURCHASE_ORDER;
import static ph.txtdis.type.ModuleType.PURCHASE_RECEIPT;
import static ph.txtdis.type.ModuleType.RETURN_ORDER;
import static ph.txtdis.type.ModuleType.SALES_ORDER;
import static ph.txtdis.type.ModuleType.SALES_RETURN;
import static ph.txtdis.type.ModuleType.STOCK_TAKE_RECONCILIATION;
import static ph.txtdis.type.ModuleType.UPLOAD;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ph.txtdis.app.AgingReceivableApp;
import ph.txtdis.app.CreditNoteApp;
import ph.txtdis.app.InventoryApp;
import ph.txtdis.app.MultiTyped;
import ph.txtdis.app.PickListApp;
import ph.txtdis.app.RemittanceApp;
import ph.txtdis.app.SalesApp;
import ph.txtdis.app.SalesRevenueApp;
import ph.txtdis.app.SalesVolumeApp;
import ph.txtdis.app.Startable;
import ph.txtdis.app.StockTakeApp;
import ph.txtdis.app.SyncApp;
import ph.txtdis.app.VatApp;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.StyleSheet;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.service.ScriptService;
import ph.txtdis.util.ServerUtil;

@Lazy
@Component("mainMenu")
public class MainMenu extends Stage {

	@Autowired
	private AgingReceivableApp agingApp;

	@Autowired
	private CreditNoteApp creditNoteApp;

	@Autowired
	private SalesApp purchasingApp, purchaseReceiptApp, bookingApp, receivingApp, returnsApp, badOrderApp, deliveryApp,
			invoiceApp;

	@Autowired
	private InventoryApp inventoryApp;

	@Autowired
	private SalesRevenueApp salesRevenueApp;

	@Autowired
	private SalesVolumeApp salesVolumeApp;

	@Autowired
	private SettingsMenu settingsMenu;

	@Autowired
	private PickListApp pickApp;

	@Autowired
	private RemittanceApp remittanceApp;

	@Autowired
	private StockTakeApp stockTakeApp;

	@Autowired
	private SalesApp stockTakeReconciliationApp;

	@Autowired
	private SyncApp upApp, downApp;

	@Autowired
	private VatApp vatApp;

	@Autowired
	private LabelFactory label;

	@Autowired
	private MessageDialog dialog;

	@Autowired
	private ScriptService scriptService;

	@Autowired
	private ServerUtil serverUtil;

	@Autowired
	private StyleSheet styleSheet;

	public void display() {
		getIcons().add(new FontIcon("\ue826"));
		setTitle("txtDIS Menu");
		setScene(createScene());
		styleSheet.update(user().getStyle());
		setOnCloseRequest(e -> checkUnpostedTransactions(e));
		show();
	}

	private String appType(Startable app) {
		return ((MultiTyped) app).type();
	}

	private AppButton button() {
		return new AppButton().fontSize(44);
	}

	private AppButton button(Startable app) {
		AppButton button = buttonType(app).build();
		button.setOnAction(event -> app.start());
		return button;
	}

	private AppButton buttonType(Startable app) {
		AppButton button = button();
		if (app instanceof MultiTyped)
			return button.icon(appType(app));
		return button.app(app);
	}

	private void checkUnpostedTransactions(WindowEvent e) {
		if (serverUtil.isOffSite() && scriptService.unpostedTransactionsExist())
			showPostOrExitDialog(e);
	}

	private Scene createScene() {
		Scene s = new Scene(dialogBox());
		s.getStylesheets().add("/css/base.css");
		return s;
	}

	private Parent dialogBox() {
		HBox b = new HBox(gridPane());
		b.setPadding(new Insets(10));
		b.setAlignment(Pos.CENTER);
		return b;
	}

	private String download() {
		return serverUtil.isOffSite() ? "Replicate" : "Download";
	}

	private GridPane gridPane() {
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setAlignment(Pos.CENTER);

		gp.add(button(purchasingApp.type(PURCHASE_ORDER)), 0, 0);
		gp.add(button(purchaseReceiptApp.type(PURCHASE_RECEIPT)), 1, 0);
		gp.add(button(returnsApp.type(RETURN_ORDER)), 2, 0);
		gp.add(button(badOrderApp.type(BAD_ORDER)), 3, 0);
		gp.add(button(bookingApp.type(SALES_ORDER)), 4, 0);
		gp.add(button(pickApp), 5, 0);
		gp.add(button(receivingApp.type(SALES_RETURN)), 6, 0);

		gp.add(label.menu("Purchases"), 0, 1);
		gp.add(label.menu("P/O Receipts"), 1, 1);
		gp.add(label.menu("Item Returns"), 2, 1);
		gp.add(label.menu("Bad Orders"), 3, 1);
		gp.add(label.menu("Bookings"), 4, 1);
		gp.add(label.menu("Picklists"), 5, 1);
		gp.add(label.menu("S/O Returns"), 6, 1);

		gp.add(button(deliveryApp.type(DELIVERY_REPORT)), 0, 2);
		gp.add(button(invoiceApp.type(INVOICE)), 1, 2);
		gp.add(button(remittanceApp), 2, 2);
		gp.add(button(agingApp), 3, 2);
		gp.add(button(vatApp), 4, 2);
		gp.add(button(salesVolumeApp), 5, 2);
		gp.add(button(salesRevenueApp), 6, 2);

		gp.add(label.menu("D/R"), 0, 3);
		gp.add(label.menu("Invoicing"), 1, 3);
		gp.add(label.menu("Collection"), 2, 3);
		gp.add(label.menu("Aging A/R"), 3, 3);
		gp.add(label.menu("VAT"), 4, 3);
		gp.add(label.menu("Volume"), 5, 3);
		gp.add(label.menu("Revenue"), 6, 3);

		gp.add(button(inventoryApp), 0, 4);
		gp.add(button(stockTakeApp), 1, 4);
		gp.add(button(stockTakeReconciliationApp.type(STOCK_TAKE_RECONCILIATION)), 2, 4);
		gp.add(button(upApp.type(UPLOAD)), 3, 4);
		gp.add(button(downApp.type(DOWNLOAD)), 4, 4);
		gp.add(button(creditNoteApp), 5, 4);
		gp.add(button(settingsMenu), 6, 4);

		gp.add(label.menu("Inventories"), 0, 5);
		gp.add(label.menu("Stock Take"), 1, 5);
		gp.add(label.menu("Stock Recon"), 2, 5);
		gp.add(label.menu(upload()), 3, 5);
		gp.add(label.menu(download()), 4, 5);
		gp.add(label.menu("Credit Note"), 5, 5);
		gp.add(label.menu("Settings"), 6, 5);
		return gp;
	}

	private void postUnpostedTransaction(WindowEvent e) {
		e.consume();
		dialog.close();
		upApp.start();
	}

	private void showPostOrExitDialog(WindowEvent we) {
		dialog.showOption("Unposted transactions exist;\nproceed, how?", "Post", "Exit");
		dialog.setOnOptionSelection(e -> postUnpostedTransaction(we));
		dialog.setOnDefaultSelection(e -> Platform.exit());
		dialog.addParent(this).start();
	}

	private String upload() {
		return serverUtil.isOffSite() ? "Post" : "Upload";
	}
}
