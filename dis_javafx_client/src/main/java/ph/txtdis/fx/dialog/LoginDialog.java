package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TEXT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ph.txtdis.fx.FontIcon;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.control.PasswordInput;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.util.LoginService;
import ph.txtdis.util.ServerUtil;

@Lazy
@Component("loginDialog")
public class LoginDialog extends Stage {

	private static final String STYLE = "-fx-font-size: 11pt; -fx-base: #6a5acd; -fx-accent: -fx-base;"
			+ " -fx-focus-color: white; -fx-faint-focus-color: #ffffff22; ";

	@Value("${server.default}")
	private String location;

	@Autowired
	private ServerUtil serverUtil;

	@Autowired
	private LoginService login;

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

	@Autowired
	private AppGridPane grid;

	private BooleanProperty ready = new SimpleBooleanProperty(false);

	@Override
	public void showAndWait() {
		setScene();
		clearFields();
		userField.requestFocus();
		ready.setValue(Boolean.TRUE);
		super.showAndWait();
	}

	private Node buttons() {
		HBox hb = new HBox(loginButton(), passwordButton(), serverButton());
		hb.setAlignment(Pos.CENTER);
		return hb;
	}

	private void changePassword() {
		passwordDialog.updateStyle(STYLE).addParent(this).start();
	}

	private void changePasswordIfAuthenticated() throws Exception {
		validate();
		changePassword();
		clearFields();
	}

	private void changeServer() {
		serverDialog.updateStyle(STYLE).addParent(this).start();
		setTitle();
		clearFields();
	}

	private void clearFields() {
		userField.clear();
		passwordField.clear();
		userField.requestFocus();
	}

	private void closeOnError(Exception e) {
		e.printStackTrace();
		dialog.show(e).updateStyle(STYLE).addParent(this).start();
		Platform.exit();
	}

	private Node gridPane() {
		grid.getChildren().clear();
		grid.add(label.field("Username"), 0, 0);
		grid.add(userField.build(TEXT), 1, 0);
		grid.add(label.field("Password"), 0, 1);
		grid.add(passwordField(), 1, 1);
		return grid;
	}

	private Node loginButton() {
		loginButton.text("Log-in").build();
		loginButton.disableIf(passwordField.isEmpty());
		loginButton.setOnAction(event -> tryLoggingInUponVerification());
		return loginButton;
	}

	private void logInIfAuthenticated() throws Exception {
		validate();
		close();
		mainMenu.display();
	}

	private Node passwordButton() {
		passwordButton.text("Alter Password").build();
		passwordButton.disableIf(passwordField.isEmpty());
		passwordButton.setOnAction(event -> tryChangingPasswordUponVerification());
		return passwordButton;
	}

	private Node passwordField() {
		passwordField.disableProperty().bind(userField.isEmpty());
		return passwordField;
	}

	private Scene scene() {
		VBox vb = new VBox(gridPane(), buttons());
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(20));
		vb.setStyle(STYLE);
		return new Scene(vb);
	}

	private String server() {
		return serverUtil.getLocation() == null ? location : serverUtil.getLocation();
	}

	private Node serverButton() {
		serverButton.text("Change Server").build();
		serverButton.setOnAction(event -> changeServer());
		return serverButton;
	}

	private void setScene() {
		getIcons().add(new FontIcon("\ue826"));
		setTitle();
		initModality(Modality.APPLICATION_MODAL);
		setScene(scene());
	}

	private void setTitle() {
		setTitle("Welcome to txtDIS@" + server() + "!");
	}

	private void tryChangingPasswordUponVerification() {
		try {
			changePasswordIfAuthenticated();
		} catch (Exception e) {
			closeOnError(e);
		}
	}

	private void tryLoggingInUponVerification() {
		try {
			logInIfAuthenticated();
		} catch (Exception e) {
			closeOnError(e);
		}
	}

	private void validate() throws Exception {
		login.validate(userField.getText(), passwordField.getText());
	}
}
