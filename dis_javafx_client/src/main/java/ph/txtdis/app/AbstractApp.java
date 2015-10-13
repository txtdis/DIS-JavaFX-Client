package ph.txtdis.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.fx.pane.AppBoxPaneFactory;
import ph.txtdis.util.Text;
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

	@Override
	public AbstractApp addParent(Stage stage) {
		if (getOwner() == null)
			initialize(stage);
		return this;
	}

	@Override
	public void refresh() {
		try {
			updateTitleAndHeader();
			setFocus();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(this).start();
		}
	}

	@Override
	public void start() {
		setStage(mainVerticalPane());
		refresh();
		show();
	}

	protected List<AppButton> addButtons() {
		return new ArrayList<>();
	}

	protected String headerText() {
		return Text.toHeader(this);
	}

	protected VBox mainVerticalPane() {
		VBox vbox = box.vbox(headerPane());
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

	protected String titleText() {
		return Text.toHeader(this);
	}

	protected String unicode() {
		return typeMap.icon(this);
	}

	protected void updateTitleAndHeader() {
		setTitle(titleText());
		if (header != null)
			header.setText(headerText());
	}

	private TilePane buttonPane() {
		TilePane tile = new TilePane();
		tile.getChildren().addAll(addButtons());
		tile.setHgap(5);
		tile.setAlignment(Pos.TOP_RIGHT);
		return tile;
	}

	private Label header() {
		return header = label.header(headerText());
	}

	private HBox headerPane() {
		TilePane buttons = buttonPane();
		HBox hBox = new HBox(header(), buttons);
		HBox.setHgrow(buttons, Priority.ALWAYS);
		hBox.setPadding(new Insets(10, 10, 0, 10));
		return hBox;
	}

	private Image icon() {
		return new FontIcon(unicode(), Color.NAVY);
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
}
