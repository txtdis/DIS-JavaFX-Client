package ph.txtdis.fx.pane;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

@Component
@Scope(value = "prototype")
public class AppGridPane extends GridPane {

	public AppGridPane() {
		setPadding(new Insets(10));
		setHgap(10);
		setVgap(10);
		setAlignment(Pos.CENTER);
	}
}
