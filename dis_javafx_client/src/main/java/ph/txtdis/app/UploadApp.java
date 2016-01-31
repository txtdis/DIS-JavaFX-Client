package ph.txtdis.app;

import static org.apache.commons.lang3.text.WordUtils.capitalizeFully;
import static org.apache.commons.lang3.text.WordUtils.uncapitalize;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.UI;
import ph.txtdis.type.ModuleType;

@Lazy
@Component("uploadApp")
public class UploadApp extends Stage implements MultiTyped, Startable {

	private interface Init {
		void complete();
	}

	private HBox splash;

	private ModuleType type;

	@Override
	public UploadApp addParent(Stage stage) {
		if (getOwner() == null)
			initialize(stage);
		return this;
	}

	@Override
	public void refresh() {
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void start() {
		createSplash();
		setScene(scene());
		Task<Void> task = initApp();
		setSplashToFadeOnInitCompletion(task, () -> task.getValue());
		showSplash();
		new Thread(task).start();
	}

	@Override
	public String type() {
		String s = type.toString();
		s = capitalizeFully(s, '_').replace("_", "");
		return uncapitalize(s);
	}

	@Override
	public Startable type(ModuleType type) {
		this.type = type;
		return this;
	}

	private void createSplash() {
		splash = new HBox(phoneInsideSpinningBallLogo(), textPane());
		splash.setAlignment(Pos.CENTER);
		splash.setPadding(new Insets(35, 25, 35, 60));
		splash.setStyle("-fx-base: slateblue; -fx-background-radius: 0.5em; ");
	}

	private void fadeSplash() {
		FadeTransition ft = new FadeTransition(Duration.seconds(1.2));
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setOnFinished(e -> hide());
		ft.play();
	}

	private Task<Void> initApp() {
		return new Task<Void>() {
			@Override
			protected Void call() throws InterruptedException {
				return null;
			}
		};
	}

	private void initialize(Stage stage) {
		initOwner(stage);
		initModality(Modality.WINDOW_MODAL);
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
		Scene s = new Scene(splash);
		s.setFill(Color.TRANSPARENT);
		return s;
	}

	private void setSplashToFadeOnInitCompletion(Task<?> task, Init init) {
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				fadeSplash();
				init.complete();
			}
		});
	}

	private void showSplash() {
		setTitle("Starting txtDIS...");
		getIcons().add(new FontIcon("\ue826"));
		initStyle(StageStyle.TRANSPARENT);
		setScene(scene());
		show();
	}

	private Node spinningBalls() {
		ProgressIndicator pi = new ProgressIndicator();
		pi.setScaleX(2.0);
		pi.setScaleY(2.0);
		pi.setStyle(" -fx-accent: white;");
		return pi;
	}

	private Parent textPane() {
		VBox hb = new VBox(trademark(), message());
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(0, 0, 0, 50));
		hb.setStyle("-fx-background: transparent;");
		return hb;
	}

	private Node trademark() {
		UI.loadFont("Ubuntu-BI");
		Label tm = new Label("txtDIS");
		tm.setStyle("-fx-font: 48pt 'ubuntu'; -fx-text-fill: midnightblue;");
		tm.setAlignment(Pos.TOP_CENTER);
		return tm;
	}
}
