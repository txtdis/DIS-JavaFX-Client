package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.control.Button;
import ph.txtdis.app.Startable;
import ph.txtdis.dto.Authority;
import ph.txtdis.dto.User;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.type.UserType;

@Scope("prototype")
@Component("userApp")
public class UserApp extends PasswordDialog implements Startable {

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<String> userField;

	@Override
	public void refresh() {
		clearPasswordFields();
		userField.clear();
		super.refresh();
	}

	@Override
	public void setFocus() {
		userField.requestFocus();
	}

	private void clearPasswordFields() {
		password1.clear();
		password2.clear();
	}

	private Authority managerRole() {
		Authority a = new Authority();
		a.setAuthority(UserType.MANAGER);
		return a;
	}

	private User newUser() {
		User user = new User();
		user.setUsername(username());
		user.setPassword(encodedPassword());
		user.setEnabled(true);
		user.setRoles(Arrays.asList(managerRole()));
		return user;
	}

	private AppField<String> userField() {
		userField.build(TEXT);
		userField.setOnAction(event -> validate());
		return userField;
	}

	private String username() {
		return userField.getText();
	}

	private void validate() {
		try {
			validateUserName(userField.getText());
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
			refresh();
		}
	}

	private void validateUserName(String username) throws Exception {
		if (service.find(username) != null)
			throw new DuplicateException(username);
	}

	@Override
	protected Button changeButton() {
		changeButton.setText("Save");
		return super.changeButton();
	}

	@Override
	protected AppGridPane grid() {
		grid.getChildren().clear();
		grid.add(label.field("Input Username"), 0, 0);
		grid.add(userField(), 1, 0);
		return super.grid();
	}

	@Override
	protected String headerText() {
		return "Add New User";
	}

	@Override
	protected void saveUser() throws Exception {
		service.save(newUser());
	}

	@Override
	protected void setBindings() {
		password1.disableIf(userField.isEmpty());
		super.setBindings();
	}
}