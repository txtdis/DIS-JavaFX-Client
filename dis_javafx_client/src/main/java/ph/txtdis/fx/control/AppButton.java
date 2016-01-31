package ph.txtdis.fx.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import ph.txtdis.app.Startable;
import ph.txtdis.fx.ToolTip;
import ph.txtdis.util.TypeMap;

@Component
@Scope("prototype")
public class AppButton extends Button {

	private String text, color, tooltip;

	private int size;

	private boolean isUnicode;

	public AppButton() {
		setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER)
				fire();
		});
	}

	public AppButton app(Startable app) {
		isUnicode = true;
		return text(new TypeMap().icon(app));
	}

	public AppButton build() {
		if (tooltip != null)
			setTooltip(new ToolTip(tooltip));
		setText(text);
		setStyle(text == null || isUnicode ? iconButtonStyle() : textButtonStyle());
		return this;
	}

	public AppButton color(String color) {
		this.color = color;
		return this;
	}

	public void disableIf(ObservableValue<Boolean> b) {
		disableProperty().bind(b);
	}

	public AppButton fontSize(int size) {
		this.size = size;
		return this;
	}

	public AppButton icon(String unicode) {
		isUnicode = true;
		return text(new TypeMap().icon(unicode));
	}

	public AppButton large(String text) {
		this.text = text;
		this.size = 14;
		HBox.setMargin(this, new Insets(0, 0, 0, 10));
		return this;
	}

	public AppButton text(String text) {
		this.text = text;
		return this;
	}

	public AppButton tooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	private String buttonSize() {
		int s = iconSize() * 2 - 2;
		return " -fx-min-width: " + s + "; -fx-max-width: " + s + "; -fx-min-height: " + s + "; -fx-max-height: " + s
				+ ";";
	}

	private String color() {
		return color == null ? "" : "-fx-text-fill: " + color + "; ";
	}

	private String iconButtonStyle() {
		return " -fx-font: " + iconSize() + " 'txtdis'; " + color() + "-fx-padding: 0; " + buttonSize();
	}

	private int iconSize() {
		return size == 0 ? 24 : size;
	}

	private int textSize() {
		return size == 0 ? 11 : size;
	}

	protected String textButtonStyle() {
		return " -fx-font-size: " + textSize() + "pt;";
	}
}
