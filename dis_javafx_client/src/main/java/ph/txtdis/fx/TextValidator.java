package ph.txtdis.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ph.txtdis.fx.control.AppField;

public class TextValidator implements ChangeListener<String> {

	private boolean ignore;

	private AppField<?> input;

	private String restrict;

	public TextValidator(AppField<?> input) {
		this.input = input;
		this.restrict = "[a-zA-Z0-9-\\[\\]*:,.&'+/!% ]";
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (ignore || newValue == null)
			return;
		if (!newValue.matches(restrict + "*")) {
			ignore = true;
			input.setText(oldValue);
			ignore = false;
		} else if (input.length() > 0 && newValue.length() > input.length()) {
			ignore = true;
			input.setText(newValue.substring(0, input.length()));
			ignore = false;
		} else {
			input.setText(newValue.toUpperCase());
		}
	}
}
