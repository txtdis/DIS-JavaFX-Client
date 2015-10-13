package ph.txtdis.fx.dialog;

import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.pane.AppGridPane;

@Scope("prototype")
@Component("searchDialog")
public class SearchDialog extends InputDialog {

	@Autowired
	protected AppGridPane grid;

	@Autowired
	private AppField<String> textField;

	@Autowired
	private AppButton findButton;

	private String searchText, criteria;

	public SearchDialog criteria(String criteria) {
		this.criteria = criteria;
		return this;
	}

	public String getText() {
		return searchText;
	}

	@Override
	public void setFocus() {
		textField.requestFocus();
	}

	private Button findButton() {
		findButton.large("Find").build();
		findButton.setOnAction(event -> setEnteredText());
		return findButton;
	}

	private AppGridPane grid() {
		grid.getChildren().clear();
		grid.add(help(), 0, 0);
		grid.add(textField.build(TEXT), 0, 1);
		return grid;
	}

	private Label help() {
		return new Label("Enter partial or full " + criteria + " to find a match; blank to list all");
	}

	private void setEnteredText() {
		searchText = textField.getText();
		System.err.println("SearchText = " + searchText);
		textField.clear();
		close();
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { findButton(), closeButton() };
	}

	@Override
	protected String headerText() {
		return "Search";
	}

	@Override
	protected List<Node> nodes() {
		return Arrays.asList(header(), grid(), buttonBox());
	}
}
