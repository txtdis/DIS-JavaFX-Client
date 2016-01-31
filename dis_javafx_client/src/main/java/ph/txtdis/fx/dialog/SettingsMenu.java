package ph.txtdis.fx.dialog;

import static javafx.stage.Modality.WINDOW_MODAL;

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
import ph.txtdis.app.ChannelApp;
import ph.txtdis.app.CustomerApp;
import ph.txtdis.app.HolidayApp;
import ph.txtdis.app.ItemApp;
import ph.txtdis.app.ItemFamilyApp;
import ph.txtdis.app.ItemTreeApp;
import ph.txtdis.app.PricingTypeApp;
import ph.txtdis.app.RouteApp;
import ph.txtdis.app.Startable;
import ph.txtdis.app.TruckApp;
import ph.txtdis.app.WarehouseApp;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;

@Lazy
@Component("settingsMenu")
public class SettingsMenu extends Stage implements Startable {

	@Autowired
	private LabelFactory label;

	@Autowired
	private ChannelApp channelApp;

	@Autowired
	private CustomerApp customerApp;

	@Autowired
	private HolidayApp holidayApp;

	@Autowired
	private ItemApp itemApp;

	@Autowired
	private ItemFamilyApp familyApp;

	@Autowired
	private ItemTreeApp treeApp;

	@Autowired
	private PricingTypeApp pricingTypeApp;

	@Autowired
	private RouteApp routeApp;

	@Autowired
	private StyleApp styleDialog;

	@Autowired
	private TruckApp truckApp;

	@Autowired
	private UserApp userDialog;

	@Autowired
	private WarehouseApp warehouseApp;

	@Override
	public SettingsMenu addParent(Stage stage) {
		initOwner(stage);
		initModality(WINDOW_MODAL);
		return this;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void setFocus() {
		requestFocus();
	}

	@Override
	public void start() {
		getIcons().add(new FontIcon("\ue801"));
		setTitle("Settings Menu");
		setScene(createScene());
		show();
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
		return button().app(app);
	}

	private Scene createScene() {
		Scene scene = new Scene(dialogBox());
		scene.getStylesheets().add("/css/base.css");
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

		gp.add(button(itemApp), 0, 0);
		gp.add(button(familyApp), 1, 0);
		gp.add(button(treeApp), 2, 0);
		gp.add(button(pricingTypeApp), 3, 0);
		gp.add(button(warehouseApp), 4, 0);
		gp.add(button(holidayApp), 5, 0);

		gp.add(label.menu("Item Master"), 0, 1);
		gp.add(label.menu("Item Family"), 1, 1);
		gp.add(label.menu("Item Tree"), 2, 1);
		gp.add(label.menu("Pricing Type"), 3, 1);
		gp.add(label.menu("Warehouses"), 4, 1);
		gp.add(label.menu("Holidays"), 5, 1);

		gp.add(button(truckApp), 0, 2);
		gp.add(button(routeApp), 1, 2);
		gp.add(button(channelApp), 2, 2);
		gp.add(button(customerApp), 3, 2);
		gp.add(button(userDialog.addParent(this)), 4, 2);
		gp.add(button(styleDialog.addParent(this)), 5, 2);

		gp.add(label.menu("Trucks"), 0, 3);
		gp.add(label.menu("Routes"), 1, 3);
		gp.add(label.menu("Channels"), 2, 3);
		gp.add(label.menu("Customers"), 3, 3);
		gp.add(label.menu("Users"), 4, 3);
		gp.add(label.menu("Styles"), 5, 3);

		return gp;
	}
}
