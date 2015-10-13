package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.PasswordInput;
import ph.txtdis.util.LoginService;
import ph.txtdis.util.ServerService;

@Component("loginDialog")
public class LoginDialog extends Stage {

	private static int tries;

	@Autowired
	private LoginService login;

	@Autowired
	private ServerService server;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<String> userField;

	@Autowired
	private PasswordInput passwordField;

	@Autowired
	private AppButton serverButton;

	@Autowired
	private AppButton passwordButton;

	@Autowired
	private AppButton loginButton;

	@Autowired
	private ServerSelectionDialog serverDialog;

	@Autowired
	private ChangePasswordDialog passwordDialog;

	@Autowired
	private MainMenu mainMenu;

	@Autowired
	private MessageDialog dialog;

	private BooleanProperty ready = new SimpleBooleanProperty(false);

	public void setIcon() {
		Image icon = new FontIcon("\ue826", Color.NAVY);
		getIcons().add(icon);
	}

	@Override
	public void showAndWait() {
		setIcon();
		setTitle();
		setScene();
		clearFields();
		userField.requestFocus();
		ready.setValue(Boolean.TRUE);
		super.showAndWait();
	}

	private Node buttons() {
		setButtons();
		setButtonsOnActionProperties();
		setButtonsDisableProperties();
		return new HBox(loginButton, passwordButton, serverButton);
	}

	private void changePassword() {
		passwordDialog.addParent(this).start();
	}

	private void changePasswordIfAuthenticated() throws Exception {
		validate();
		changePassword();
		clearFields();
	}

	private void changeServer() {
		serverDialog.addParent(this).start();
		setTitle();
	}

	private void clearFields() {
		userField.clear();
		passwordField.clear();
		userField.requestFocus();
	}

	private Node gridPane() {
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setAlignment(Pos.CENTER);
		gp.add(label.field("Username"), 0, 0);
		gp.add(userField.build(TEXT), 1, 0);
		gp.add(label.field("Password"), 0, 1);
		gp.add(passwordField, 1, 1);
		gp.setPadding(new Insets(0, 0, 10, 0));
		return gp;
	}

	private Node inputBox() {
		VBox vb = new VBox(gridPane(), buttons());
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(0, 0, 0, 50));
		return vb;
	}

	private void logInIfAuthenticated() throws Exception {
		validate();
		close();
		mainMenu.display();
	}

	private Parent parentPane() {
		HBox hb = new HBox(phoneInsideSpinningBallLogo(), inputBox());
		hb.setPadding(new Insets(30, 20, 30, 50));
		hb.setAlignment(Pos.CENTER);
		hb.setStyle("-fx-font-size: 11pt; -fx-base: #6a5acd; -fx-accent: -fx-base; -fx-focus-color: #ffffff; "
				+ "  -fx-faint-focus-color: #ffffff22; ");
		return hb;
	}

	private Node phoneInsideSpinningBallLogo() {
		return new StackPane(phoneLogo(), spinningBalls());
	}

	private Node phoneLogo() {
		Label p = new Label("\ue826");
		p.setStyle("-fx-font: 72 'txtdis'; -fx-text-fill: navy;");
		p.setPadding(new Insets(10));
		return p;
	}

	private void retryThrice(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		clearFields();
		if (++tries > 2)
			close();
	}

	private void setButtons() {
		serverButton.text("Change Server").build();
		passwordButton.text("Alter Password").build();
		loginButton.text("Log-in").build();
	}

	private void setButtonsDisableProperties() {
		passwordField.disableProperty().bind(userField.isEmpty());
		loginButton.disableIf(passwordField.isEmpty());
		passwordButton.disableIf(passwordField.isEmpty());
	}

	private void setButtonsOnActionProperties() {
		serverButton.setOnAction(event -> changeServer());
		passwordButton.setOnAction(event -> tryChangingPasswordUponVerification());
		loginButton.setOnAction(event -> tryLoggingInUponVerification());
	}

	private void setScene() {
		initModality(Modality.APPLICATION_MODAL);
		setScene(new Scene(parentPane()));
	}

	private void setTitle() {
		setTitle("Welcome to txtDIS@" + server.name() + "!");
	}

	private Node spinningBalls() {
		ProgressIndicator pi = new ProgressIndicator(-1.0);
		pi.setScaleX(2.0);
		pi.setScaleY(2.0);
		pi.setStyle(" -fx-accent: #ffffff;");
		return pi;
	}

	private void tryChangingPasswordUponVerification() {
		try {
			changePasswordIfAuthenticated();
		} catch (Exception e) {
			retryThrice(e);
		}
	}

	private void tryLoggingInUponVerification() {
		try {
			logInIfAuthenticated();
		} catch (Exception e) {
			retryThrice(e);
		}
	}

	private void validate() throws Exception {
		login.validate(server.name(), userField.getText(), passwordField.getText());
	}
}
