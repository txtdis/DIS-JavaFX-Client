package ph.txtdis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javafx.animation.FadeTransition;
import javafx.application.Application;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.UI;
import ph.txtdis.fx.dialog.LoginDialog;

@SpringBootApplication
public class App extends Application {

	private interface Init {
		void complete();
	}

	public static void main(String[] args) {
		launch();
	}

	private HBox splash;

	@Override
	public void init() throws Exception {
		createSplash();
	}

	@Override
	public void start(Stage stage) throws Exception {
		Task<ApplicationContext> task = initApp();
		setSplashToFadeOnInitCompletion(stage, task, () -> showLoginDialog(task.getValue()));
		showSplash(stage);
		new Thread(task).start();
	}

	private void createSplash() {
		splash = new HBox(phoneInsideSpinningBallLogo(), textPane());
		splash.setAlignment(Pos.CENTER);
		splash.setPadding(new Insets(35, 25, 35, 60));
		splash.setStyle("-fx-base: slateblue; -fx-background-radius: 0.5em; ");
	}

	private void fadeSplash(Stage stage) {
		FadeTransition ft = new FadeTransition(Duration.seconds(1.2), splash);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setOnFinished(e -> stage.hide());
		ft.play();
	}

	private Task<ApplicationContext> initApp() {
		return new Task<ApplicationContext>() {
			@Override
			protected ApplicationContext call() throws InterruptedException {
				return SpringApplication.run(App.class);
			}
		};
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

	private void setSplashToFadeOnInitCompletion(Stage stage, Task<?> task, Init init) {
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				fadeSplash(stage);
				init.complete();
			}
		});
	}

	private void showLoginDialog(ApplicationContext context) {
		LoginDialog ld = context.getBean(LoginDialog.class);
		ld.showAndWait();
	}

	private void showSplash(Stage stage) {
		stage.setTitle("Starting txtDIS...");
		stage.getIcons().add(new FontIcon("\ue826"));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene());
		stage.show();
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
