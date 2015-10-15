package ph.txtdis.fx.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ph.txtdis.app.AgingReceivableApp;
import ph.txtdis.app.ChannelApp;
import ph.txtdis.app.CustomerApp;
import ph.txtdis.app.InvoiceApp;
import ph.txtdis.app.InvoiceBookletApp;
import ph.txtdis.app.ItemFamilyApp;
import ph.txtdis.app.ItemTreeApp;
import ph.txtdis.app.PickingApp;
import ph.txtdis.app.RouteApp;
import ph.txtdis.app.Startable;
import ph.txtdis.app.TruckApp;
import ph.txtdis.app.VatApp;
import ph.txtdis.app.WarehouseApp;
import ph.txtdis.dto.Style;
import ph.txtdis.dto.User;
import ph.txtdis.fx.AppIcon;
import ph.txtdis.fx.StyleSheet;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.util.Spring;

@Lazy
@Component("mainMenu")
public class MainMenu extends Stage {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AgingReceivableApp agingApp;

	@Autowired
	private BackupApp backupDialog;

	@Autowired
	private InvoiceApp bookingApp;

	@Autowired
	private ChannelApp channelApp;

	@Autowired
	private CustomerApp customerApp;

	@Autowired
	private VatApp dayEndApp;

	@Autowired
	private InvoiceApp deliveryApp;

	@Autowired
	private VatApp inventoryApp;

	@Autowired
	private InvoiceApp invoiceApp;

	@Autowired
	private InvoiceBookletApp invoiceBookletApp;

	@Autowired
	private CustomerApp itemApp;

	@Autowired
	private ItemFamilyApp familyApp;

	@Autowired
	private ItemTreeApp treeApp;

	@Autowired
	private PickingApp pickApp;

	@Autowired
	private InvoiceApp purchaseApp;

	@Autowired
	private InvoiceApp receiptApp;

	@Autowired
	private InvoiceApp remittanceApp;

	@Autowired
	private InvoiceApp returnApp;

	@Autowired
	private UserApp roleDialog;

	@Autowired
	private RouteApp routeApp;

	@Autowired
	private VatApp salesApp;

	@Autowired
	private InvoiceApp stockTakeApp;

	@Autowired
	private InvoiceApp stockTakeReconciliationApp;

	@Autowired
	private StyleApp styleDialog;

	@Autowired
	private TruckApp truckApp;

	@Autowired
	private UserApp userDialog;

	@Autowired
	private VatApp vatApp;

	@Autowired
	private WarehouseApp warehouseApp;

	@Autowired
	private StyleSheet styleSheet;

	public void display() {
		setTitleBar();
		setScene(createScene());
		show();
	}

	private AppButton button(BackupApp backup) {
		AppButton button = newButton().icon("backup").build();
		button.setOnAction(event -> backup.chooseFolder(this));
		return button;
	}

	private AppButton button(Startable app) {
		AppButton button = newButton().app(app).build();
		button.setOnAction(event -> app.start());
		return button;
	}

	private AppButton button(Startable app, Stage stage) {
		AppButton button = newButton().app(app).build();
		button.setOnAction(event -> app.addParent(stage).start());
		return button;
	}

	private Scene createScene() {
		Scene scene = new Scene(dialogBox());
		scene.getStylesheets().add("/css/base.css");
		setSceneStyle();
		return scene;
	}

	private Parent dialogBox() {
		HBox box = new HBox(gridPane());
		box.setPadding(new Insets(10));
		box.setAlignment(Pos.CENTER);
		return box;
	}

	private GridPane gridPane() {
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setAlignment(Pos.CENTER);

		gp.add(button(purchaseApp), 0, 0);
		gp.add(button(receiptApp), 1, 0);
		gp.add(button(bookingApp), 2, 0);
		gp.add(button(returnApp), 3, 0);
		gp.add(button(pickApp), 4, 0);
		gp.add(button(deliveryApp), 5, 0);
		gp.add(button(agingApp), 6, 0);

		gp.add(label.menu("Purchasing"), 0, 1);
		gp.add(label.menu("Receiving"), 1, 1);
		gp.add(label.menu("Booking"), 2, 1);
		gp.add(label.menu("RMA"), 3, 1);
		gp.add(label.menu("Picking"), 4, 1);
		gp.add(label.menu("Delivery"), 5, 1);
		gp.add(label.menu("Aging A/R"), 6, 1);

		gp.add(button(itemApp), 0, 2);
		gp.add(button(familyApp), 1, 2);
		gp.add(button(treeApp), 2, 2);
		gp.add(button(warehouseApp), 3, 2);
		gp.add(button(inventoryApp), 4, 2);
		gp.add(button(stockTakeApp), 5, 2);
		gp.add(button(stockTakeReconciliationApp), 6, 2);

		gp.add(label.menu("Item Master"), 0, 3);
		gp.add(label.menu("Item Family"), 1, 3);
		gp.add(label.menu("Item Tree"), 2, 3);
		gp.add(label.menu("Warehouse"), 3, 3);
		gp.add(label.menu("Inventory"), 4, 3);
		gp.add(label.menu("Stock Take"), 5, 3);
		gp.add(label.menu("Stock Recon"), 6, 3);

		gp.add(button(truckApp), 0, 4);
		gp.add(button(routeApp), 1, 4);
		gp.add(button(channelApp), 2, 4);
		gp.add(button(customerApp), 3, 4);
		gp.add(button(invoiceBookletApp), 4, 4);
		gp.add(button(invoiceApp), 5, 4);
		gp.add(button(remittanceApp), 6, 4);

		gp.add(label.menu("Truck"), 0, 5);
		gp.add(label.menu("Route"), 1, 5);
		gp.add(label.menu("Channel"), 2, 5);
		gp.add(label.menu("Customer"), 3, 5);
		gp.add(label.menu("S/I Booklet"), 4, 5);
		gp.add(label.menu("Invoice"), 5, 5);
		gp.add(label.menu("Remittance"), 6, 5);

		gp.add(button(userDialog, this), 0, 6);
		gp.add(button(roleDialog, this), 1, 6);
		gp.add(button(styleDialog, this), 2, 6);
		gp.add(button(backupDialog), 3, 6);
		gp.add(button(vatApp), 4, 6);
		gp.add(button(dayEndApp), 5, 6);
		gp.add(button(salesApp), 6, 6);

		gp.add(label.menu("User"), 0, 7);
		gp.add(label.menu("Role"), 1, 7);
		gp.add(label.menu("Style"), 2, 7);
		gp.add(label.menu("Backup"), 3, 7);
		gp.add(label.menu("VAT"), 4, 7);
		gp.add(label.menu("Day-End"), 5, 7);
		gp.add(label.menu("Month-End"), 6, 7);

		return gp;
	}

	private AppButton newButton() {
		return new AppButton().fontSize(44);
	}

	private void setSceneStyle() {
		styleSheet.update(style());
	}

	private void setTitleBar() {
		getIcons().add(new AppIcon());
		setTitle("txtDIS Menu");
	}

	private Style style() {
		return user().getStyle();
	}

	private User user() {
		return Spring.user();
	}
}
