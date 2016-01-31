package ph.txtdis.fx.control;

import static javafx.geometry.HPos.RIGHT;

import org.springframework.stereotype.Component;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

@Component("label")
public class LabelFactory {

	public Label dialog(String text) {
		Label label = header(text);
		label.setPadding(new Insets(20, 0, 0, 0));
		return label;
	}

	public Label field(String name) {
		Label label = new Label(name);
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		GridPane.setHalignment(label, RIGHT);
		return label;
	}

	public Label group(String name) {
		Label label = new Label(name);
		label.setStyle(" -fx-font-size: 16pt;");
		return label;
	}

	public Label header(String text) {
		Label label = new Label(text);
		label.setStyle(" -fx-font-size: 26pt;");
		return label;
	}

	public Label icon(String unicode, String color) {
		Label label = new Label(unicode);
		label.setStyle(" -fx-font: 90pt 'txtdis'; -fx-text-fill: " + color + ";");
		label.setPadding(new Insets(20, 0, 20, 0));
		return label;
	}

	public Label menu(String name) {
		Label label = new Label(name);
		GridPane.setHalignment(label, HPos.CENTER);
		return label;
	}

	public Label message(String text) {
		Label label = new Label(text);
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setPrefHeight(75);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return label;
	}

	public Label name(String name) {
		return new Label(name);
	}

	public Label subheader(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-size: 18pt;");
		label.setAlignment(Pos.CENTER);
		return label;
	}
}
