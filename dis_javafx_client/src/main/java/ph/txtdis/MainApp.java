package ph.txtdis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import ph.txtdis.fx.dialog.LoginDialog;

@SpringBootApplication
public class MainApp extends Application {

	public static void main(String[] args) {
		launch();
	}

	private LoginDialog loginDialog;

	@Override
	public void init() throws Exception {
		Platform.runLater(() -> {
			ConfigurableApplicationContext context = SpringApplication.run(MainApp.class, "--debug");
			loginDialog = context.getBean(LoginDialog.class);
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		loginDialog.showAndWait();
	}
}
