package ph.txtdis;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.UI;

public class AppPreloader extends Preloader {

	private Stage stage;

	@Override
	public void handleStateChangeNotification(StateChangeNotification evt) {
		if (evt.getType() == Type.BEFORE_START)
			stage.hide();
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("txtDIS");
		stage.getIcons().add(new FontIcon("\ue826", Color.NAVY));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(new Scene(parentPane()));
		stage.show();
	}

	private Node message() {
		Label message = new Label("Please wait...");
		message.setStyle("-fx-font: 11pt 'ubuntu'; ");
		message.setPadding(new Insets(0, 0, 5, 0));
		return message;
	}

	private Parent parentPane() {
		HBox hb = new HBox(phoneLogo(), textPane());
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(30, 40, 30, 40));
		hb.setStyle("-fx-base: slateblue; -fx-background-radius: 0.5em; ");
		return hb;
	}

	private Label phoneLogo() {
		Label p = new Label("\ue826");
		p.setStyle("-fx-font: 72 'txtdis'; -fx-text-fill: navy; ");
		p.setPadding(new Insets(0, 15, 0, 0));
		p.setAlignment(Pos.CENTER);
		return p;
	}

	private ProgressBar progressBar() {
		ProgressBar bar = new ProgressBar();
		bar.setStyle(" -fx-accent: navy; -fx-background: white;");
		return bar;
	}

	private Parent textPane() {
		VBox hb = new VBox(trademark(), message(), progressBar());
		hb.setAlignment(Pos.CENTER);
		hb.setStyle("-fx-background: transparent;");
		return hb;
	}

	private Node trademark() {
		UI.loadFont("Ubuntu-BI");
		Label trademark = new Label("txtDIS");
		trademark.setStyle("-fx-font: 38pt 'ubuntu'; -fx-text-fill: navy;");
		trademark.setAlignment(Pos.TOP_CENTER);
		return trademark;
	}

}
