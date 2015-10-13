package ph.txtdis.fx.pane;

import org.springframework.stereotype.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

@Component
public class DialogMessageBox extends VBox {

    public DialogMessageBox() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(0, 0, 0, 20));
    }

    public DialogMessageBox addNodes(Node... nodes) {
        getChildren().setAll(nodes);
        return this;
    }

}
