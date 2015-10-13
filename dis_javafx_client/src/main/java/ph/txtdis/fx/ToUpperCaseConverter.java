package ph.txtdis.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ph.txtdis.fx.control.AppField;

public class ToUpperCaseConverter implements ChangeListener<String> {

	private boolean ignore;

	private int maxLength;

	private AppField<?> input;

	public ToUpperCaseConverter(AppField<?> input) {
		this.input = input;
		maxLength = (int) input.getMinWidth() / 20;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (ignore || newValue == null)
			return;
		if (newValue.length() > maxLength) {
			ignore = true;
			input.setText(newValue.substring(0, maxLength));
			ignore = false;
		} else {
			input.setText(newValue.toUpperCase());
		}
	}
}
