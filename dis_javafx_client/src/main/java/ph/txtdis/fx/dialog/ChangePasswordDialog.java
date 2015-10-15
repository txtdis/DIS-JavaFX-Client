package ph.txtdis.fx.dialog;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import ph.txtdis.fx.pane.AppGridPane;

@Lazy
@Component("changePasswordDialog")
public class ChangePasswordDialog extends PasswordDialog {

	@Override
	protected AppGridPane grid() {
		grid.getChildren().clear();
		return super.grid();
	}

	@Override
	protected String headerText() {
		return "Change Password";
	}
}