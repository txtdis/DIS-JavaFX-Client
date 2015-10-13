package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import ph.txtdis.fx.control.RadioControl;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.util.ServerService;

@Component("serverSelectionDialog")
public class ServerSelectionDialog extends InputDialog {

	@Autowired
	private ServerService server;

	@Autowired
	protected AppGridPane grid;

	private ToggleGroup group;

	@Override
	public void setFocus() {
		closeButton.requestFocus();
	}

	private void putButtonOnTheGrid(List<String> list) {
		grid.getChildren().clear();
		for (int i = 0; i < list.size(); i++)
			grid.add(radioButton(list.get(i)), 0, i);
	}

	private RadioControl radioButton(String s) {
		RadioControl radio = new RadioControl(s);
		radio.setToggleGroup(group);
		return radio;
	}

	private void selectTheDefaultServer() {
		group.getToggles().stream().filter(t -> ((RadioButton) t).getText().equals(server.name())).findAny().get()
				.setSelected(true);
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { button("OK") };
	}

	@Override
	protected String headerText() {
		return "Choose Server";
	}

	@Override
	protected List<Node> nodes() {
		group = new ToggleGroup();
		putButtonOnTheGrid(server.list());
		selectTheDefaultServer();
		return Arrays.asList(header(), grid, buttonBox());
	}

	@Override
	protected void setOnFiredCloseButton() {
		RadioButton rb = (RadioButton) group.getSelectedToggle();
		server.name(rb.getText());
		super.setOnFiredCloseButton();
	}
}