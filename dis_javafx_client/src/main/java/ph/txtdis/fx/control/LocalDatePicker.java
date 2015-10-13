package ph.txtdis.fx.control;

import java.time.LocalDate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.binding.BooleanBinding;
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

	public void clear() {
		setValue(null);
	}

	public BooleanBinding isEmpty() {
		return getEditor().textProperty().isEmpty();
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
