package ph.txtdis.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ph.txtdis.fx.control.AppField;

public class IntegerInputValidator implements ChangeListener<String> {

	private boolean ignore;

	private AppField<?> input;

	private String restrict;

	public IntegerInputValidator(AppField<?> input) {
		this.input = input;
		this.restrict = "[0-9-()]";
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (ignore || newValue == null)
			return;
		if (!newValue.matches(restrict + "*") || (newValue.length() > 1 && newValue.endsWith("-"))) {
			ignore = true;
			input.setText(oldValue);
			ignore = false;
		} else if (newValue.length() > 11) {
			ignore = true;
			input.setText(newValue.substring(0, 11));
			ignore = false;
		}
	}
}
