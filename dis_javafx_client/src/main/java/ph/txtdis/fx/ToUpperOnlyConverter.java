package ph.txtdis.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ph.txtdis.fx.control.AppField;

public class ToUpperOnlyConverter implements ChangeListener<String> {

	private boolean ignore;

	private int maxLength;
	
	private String restrict;

	private AppField<?> input;

	public ToUpperOnlyConverter(AppField<?> input) {
		this.input = input;
		this.restrict = "[A-Z]";
		maxLength = (int) input.getMinWidth() / 20;
	}

	@Override
	public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (ignore || newValue == null)
			return;
		if (!newValue.matches(restrict + "*")) {
			ignore = true;
			input.setText(oldValue);
			ignore = false;
		} else if (newValue.length() > maxLength) {
			ignore = true;
			input.setText(newValue.substring(0, maxLength));
			ignore = false;
		} else {
			input.setText(newValue.toUpperCase());
		}
	}
}
