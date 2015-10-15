package ph.txtdis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

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
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
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
		Task<ConfigurableApplicationContext> task = initApp();
		setSplashToFadeOnInitCompletion(stage, task, () -> showLoginDialog(task.getValue()));
		showSplash(stage);
		new Thread(task).start();
	}

	private void createSplash() {
		splash = new HBox(phoneLogo(), textPane());
		splash.setAlignment(Pos.CENTER);
		splash.setPadding(new Insets(30, 40, 30, 40));
		splash.setStyle("-fx-base: slateblue; -fx-background-radius: 0.5em; ");
	}

	private void fadeSplash(Stage stage) {
		FadeTransition ft = new FadeTransition(Duration.seconds(1.2), splash);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setOnFinished(e -> stage.hide());
		ft.play();
	}

	private Task<ConfigurableApplicationContext> initApp() {
		return new Task<ConfigurableApplicationContext>() {
			@Override
			protected ConfigurableApplicationContext call() throws InterruptedException {
				return SpringApplication.run(App.class, "--debug");
			}
		};
	}

	private Node message() {
		Label l = new Label("Please wait...");
		l.setStyle("-fx-font: 11pt 'ubuntu'; ");
		l.setPadding(new Insets(0, 0, 5, 0));
		return l;
	}

	private Label phoneLogo() {
		Label l = new Label("\ue826");
		l.setStyle("-fx-font: 72 'txtdis'; -fx-text-fill: navy; ");
		l.setPadding(new Insets(0, 15, 0, 0));
		l.setAlignment(Pos.CENTER);
		return l;
	}

	private ProgressBar progressBar() {
		ProgressBar pb = new ProgressBar();
		pb.setStyle(" -fx-accent: navy; -fx-background: white;");
		return pb;
	}

	private void setSplashToFadeOnInitCompletion(Stage stage, Task<?> task, Init init) {
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				fadeSplash(stage);
				init.complete();
			}
		});
	}

	private void showLoginDialog(ConfigurableApplicationContext context) {
		LoginDialog ld = context.getBean(LoginDialog.class);
		ld.showAndWait();
	}

	private void showSplash(Stage stage) {
		stage.setTitle("txtDIS");
		stage.getIcons().add(new FontIcon("\ue826", Color.NAVY));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(new Scene(splash));
		stage.show();
	}

	private Parent textPane() {
		VBox hb = new VBox(trademark(), message(), progressBar());
		hb.setAlignment(Pos.CENTER);
		hb.setStyle("-fx-background: transparent;");
		return hb;
	}

	private Node trademark() {
		UI.loadFont("Ubuntu-BI");
		Label tm = new Label("txtDIS");
		tm.setStyle("-fx-font: 38pt 'ubuntu'; -fx-text-fill: navy;");
		tm.setAlignment(Pos.TOP_CENTER);
		return tm;
	}
}
