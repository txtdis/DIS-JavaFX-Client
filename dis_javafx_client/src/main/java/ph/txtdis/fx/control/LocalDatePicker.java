package ph.txtdis.fx.control;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ph.txtdis.fx.DateInputValidator;

@Component
@Scope(value = "prototype")
@SuppressWarnings("restriction")
public class LocalDatePicker extends DatePicker implements InputControl<LocalDate> {

	public LocalDatePicker() {
		setStyle("-fx-opacity: 1; ");
		setPickerWidth(140);
		traverseOnPressedEnterKey();
		setPromptText("08/08/2008");
		getEditor().textProperty().addListener(new DateInputValidator(this));
	}

	@Override
	public void clear() {
		setValue(null);
	}

	public void disableIf(ObservableBooleanValue b) {
		disableProperty().bind(b);
	}

	public void editableIf(BooleanBinding b) {
		editableProperty().bind(b);
		focusTraversableProperty().bind(editableProperty());
	}

	public BooleanBinding isEmpty() {
		return valueProperty().isNull();
	}

	private void setPickerWidth(double w) {
		setMinWidth(w);
		setPrefWidth(w);
		setMaxWidth(w);
	}

	private void traverseOnPressedEnterKey() {
		addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				TextField textField = getEditor();
				TextFieldBehavior behavior = ((TextFieldSkin) textField.getSkin()).getBehavior();
				behavior.traverseNext();
			}
		});
	}
}
