package ph.txtdis.fx.control;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
@Scope("prototype")
@SuppressWarnings("restriction")
public class AppCombo<T> extends ComboBox<T> {

	public AppCombo() {
		traversePressedEnterKey();
		itemsProperty().addListener(o -> selectFirstAndDisableFocusTranversingIfSelectionIsOne());
	}

	private void selectFirstAndDisableFocusTranversingIfSelectionIsOne() {
		select(0);
		if (size() == 1)
			focusTraversableProperty().set(false);
	}

	public AppCombo<T> items(T[] items) {
		return items(Arrays.asList(items));
	}

	public AppCombo<T> items(List<T> items) {
		setItems(items == null ? FXCollections.emptyObservableList() : FXCollections.observableArrayList(items));
		return this;
	}

	public AppCombo<T> width(int width) {
		setMinWidth(width);
		return this;
	}

	public AppCombo<T> readOnlyOfWidth(int width) {
		focusTraversableProperty().set(false);
		width(width);
		return this;
	}

	public void clear() {
		getSelectionModel().clearSelection();
	}

	public void empty() {
		getItems().clear();
	}

	public int size() {
		return getItems().size();
	}

	public void select(T selection) {
		getSelectionModel().select(selection);
	}

	public void select(int index) {
		getSelectionModel().select(index);
	}

	public BooleanBinding isEmpty() {
		return getSelectionModel().selectedItemProperty().isNull();
	}

	public BooleanBinding isNotEmpty() {
		return isEmpty().not();
	}

	public BooleanBinding is(T item) {
		return getSelectionModel().selectedItemProperty().isEqualTo(item);
	}

	public BooleanBinding isNot(T item) {
		return is(item).not();
	}

	public void disableIf(BooleanBinding b) {
		disableProperty().bind(b);
	}

	public ObservableBooleanValue isDisabledNow() {
		return disabledProperty();
	}

	@SuppressWarnings("unchecked")
	private void traversePressedEnterKey() {
		addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				ComboBoxListViewSkin<T> skin = (ComboBoxListViewSkin<T>) getSkin();
				ComboBoxBaseBehavior<T> behavior = skin.getBehavior();
				behavior.traverseNext();
			}
		});
	}
}
