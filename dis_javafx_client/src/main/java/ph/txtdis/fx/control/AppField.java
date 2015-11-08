package ph.txtdis.fx.control;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.util.TypeStyle.align;
import static ph.txtdis.util.TypeStyle.style;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.TextField;
import ph.txtdis.type.Type;
import ph.txtdis.util.TypeStyle;

@Component
@Scope("prototype")
@SuppressWarnings("restriction")
public class AppField<T> extends TextField implements ErrorHandling, InputControl<T>, StylableTextField {

	private Type type;

	private int width;

	public AppField() {
		setStyle("-fx-opacity: 1; ");
		traverseOnPressedEnterKey();
		cancelEditOnLostFocus();
	}

	public AppField<T> build(Type type) {
		this.type = type;
		setAlignment();
		setFieldWidth(width());
		setProperties();
		return this;
	}

	public ObservableBooleanValue disabled() {
		return disabledProperty();
	}

	public void disableIf(BooleanBinding b) {
		disableProperty().bind(b);
	}

	@Override
	public T getValue() {
		return TypeStyle.parse(type, getText());
	}

	@Override
	public void handleError() {
		clear();
		requestFocus();
	}

	public BooleanBinding isEmpty() {
		return textProperty().isEmpty();
	}

	public BooleanBinding isNot(String text) {
		return textProperty().isNotEqualTo(text);
	}

	public BooleanBinding isNotEmpty() {
		return isEmpty().not();
	}

	public AppField<T> readOnly() {
		disableProperty().set(true);
		return this;
	}

	@Override
	public void setValue(T value) {
		style(type, this, value);
	}

	public AppField<T> width(int width) {
		this.width = width;
		return this;
	}

	private void cancelEditOnLostFocus() {
		focusedProperty().addListener((focus, outOfFocus, inFocus) -> {
			if (!inFocus)
				cancelEdit();
		});
	}

	private void setAlignment() {
		align(type, this);
	}

	private void setFieldWidth(int width) {
		setMinWidth(width);
		setPrefWidth(width);
		if (type != TEXT)
			setMaxWidth(width);
	}

	private void setProperties() {
		if (!isDisabled())
			TypeStyle.validate(type, this);
	}

	private void traverseOnPressedEnterKey() {
		addEventFilter(KEY_PRESSED, event -> {
			if (event.getCode() == ENTER)
				((TextFieldSkin) getSkin()).getBehavior().traverseNext();
		});
	}

	private int width() {
		return width != 0 ? width : TypeStyle.width(type);
	}
}
