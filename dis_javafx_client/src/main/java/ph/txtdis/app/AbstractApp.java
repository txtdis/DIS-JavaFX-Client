package ph.txtdis.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.fx.pane.AppBoxPaneFactory;
import ph.txtdis.util.TypeMap;

public abstract class AbstractApp extends Stage implements Startable {

	@Autowired
	protected TypeMap typeMap;

	@Autowired
	protected MessageDialog dialog;

	@Autowired
	protected LabelFactory label;

	@Autowired
	protected AppBoxPaneFactory box;

	private Label header;

	private TilePane buttons;

	@Override
	public AbstractApp addParent(Stage stage) {
		if (getOwner() == null)
			initialize(stage);
		return this;
	}

	@Override
	public void refresh() {
		updateTitleAndHeader();
		setFocus();
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		refresh();
		show();
	}

	private Label header() {
		return header = label.header(getHeaderText());
	}

	private HBox headerPane() {
		setButtonPane();
		HBox hBox = box.forHorizontals(header(), buttons);
		HBox.setHgrow(buttons, Priority.ALWAYS);
		hBox.setPadding(new Insets(10, 10, 0, 10));
		return hBox;
	}

	private Image icon() {
		return new FontIcon(unicode());
	}

	private void initialize(Stage stage) {
		initOwner(stage);
		initModality(Modality.WINDOW_MODAL);
	}

	private Scene scene(VBox box) {
		Scene scene = new Scene(box);
		scene.getStylesheets().addAll("/css/base.css");
		return scene;
	}

	private void setBounds() {
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		setMaxHeight(bounds.getHeight());
		setMaxWidth(bounds.getWidth());
	}

	private void setButtonPane() {
		buttons = new TilePane();
		buttons.getChildren().addAll(addButtons());
		buttons.setHgap(5);
		buttons.setAlignment(Pos.TOP_RIGHT);
	}

	protected List<AppButton> addButtons() {
		return new ArrayList<>();
	}

	protected ObservableList<Node> buttons() {
		return buttons.getChildren();
	}

	protected abstract String getHeaderText();

	protected VBox mainVerticalPane() {
		VBox vbox = box.forVerticals(headerPane());
		vbox.getChildren().addAll(mainVerticalPaneNodes());
		return vbox;
	}

	protected abstract List<Node> mainVerticalPaneNodes();

	protected void setStage(VBox box) {
		getIcons().add(icon());
		updateTitleAndHeader();
		setScene(scene(box));
		setBounds();
	}

	protected abstract String getTitleText();

	protected String unicode() {
		return typeMap.icon(this);
	}

	protected void updateTitleAndHeader() {
		setTitle(getTitleText());
		if (header != null)
			header.setText(getHeaderText());
	}
}
