package ph.txtdis.fx.pane;

import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

@Component
public class MessageDialogButtonBox extends HBox {

    public MessageDialogButtonBox() {
        setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(10, 0, 0, 0));
    }

    public MessageDialogButtonBox addButtons(Button... buttons) {
        getChildren().setAll(buttons);
        return this;
    }

}
