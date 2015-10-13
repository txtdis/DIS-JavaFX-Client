package ph.txtdis.fx.pane;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

@Component
public class TabBox extends TabPane {

    public TabBox() {
        setStyle("-fx-tab-min-width: 80;");
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    }

    public TabBox addTabs(List<Tab> tabs) {
        getTabs().addAll(tabs);
        return this;
    }
}
