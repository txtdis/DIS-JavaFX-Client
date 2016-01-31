package ph.txtdis.fx.dialog;

import static javafx.stage.Modality.WINDOW_MODAL;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ph.txtdis.fx.UI;

@Scope("prototype")
@Component("progressDialog")
public class ProgressDialog extends Stage {

	public ProgressDialog addParent(Stage stage) {
		if (getOwner() == null)
			initialize(stage);
		return this;
	}

	public void start() {
		setScene(scene());
		show();
	}

	private void initialize(Stage stage) {
		initOwner(stage);
		initModality(WINDOW_MODAL);
		initStyle(StageStyle.TRANSPARENT);
	}

	private Node message() {
		Label l = new Label("Please wait...");
		l.setStyle("-fx-font: 12pt 'ubuntu'; ");
		return l;
	}

	private Node phoneInsideSpinningBallLogo() {
		return new StackPane(phoneLogo(), spinningBalls());
	}

	private Node phoneLogo() {
		Label p = new Label("\ue826");
		p.setStyle("-fx-font: 72 'txtdis'; -fx-text-fill: midnightblue;");
		p.setPadding(new Insets(10));
		return p;
	}

	private Scene scene() {
		Scene s = new Scene(splash());
		s.getStylesheets().addAll("/css/base.css");
		s.setFill(Color.TRANSPARENT);
		return s;
	}

	private Node spinningBalls() {
		ProgressIndicator pi = new ProgressIndicator();
		pi.setScaleX(2.0);
		pi.setScaleY(2.0);
		pi.setStyle(" -fx-accent: white;");
		return pi;
	}

	private HBox splash() {
		HBox splash = new HBox(phoneInsideSpinningBallLogo(), textPane());
		splash.setAlignment(Pos.CENTER);
		splash.setPadding(new Insets(35, 25, 35, 60));
		splash.setStyle("-fx-base: slateblue; -fx-background-radius: 0.5em; ");
		return splash;

	}

	private Parent textPane() {
		VBox b = new VBox(trademark(), message());
		b.setAlignment(Pos.CENTER);
		b.setPadding(new Insets(0, 0, 0, 50));
		b.setStyle("-fx-background: transparent;");
		return b;
	}

	private Node trademark() {
		UI.loadFont("Ubuntu-BI");
		Label tm = new Label("txtDIS");
		tm.setStyle("-fx-font: 48pt 'ubuntu'; -fx-text-fill: midnightblue;");
		tm.setAlignment(Pos.TOP_CENTER);
		return tm;
	}
}
