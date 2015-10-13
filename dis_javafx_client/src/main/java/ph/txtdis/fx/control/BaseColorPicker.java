package ph.txtdis.fx.control;

import org.springframework.stereotype.Component;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

@Component
public class BaseColorPicker extends ColorPicker {

	private static Color defaultColor() {
		return Color.SLATEBLUE;
	}

	public BaseColorPicker() {
		super(defaultColor());
	}

	public void reset() {
		setValue(defaultColor());
	}
}
