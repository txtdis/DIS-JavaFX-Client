package ph.txtdis.fx.dialog;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import ph.txtdis.dto.Style;
import ph.txtdis.dto.User;
import ph.txtdis.fx.StyleSheet;
import ph.txtdis.fx.UI;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppCombo;
import ph.txtdis.fx.control.BaseColorPicker;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.pane.AppGridPane;
import ph.txtdis.service.UserService;
import ph.txtdis.util.Spring;

@Component("styleApp")
public class StyleApp extends InputDialog {

	@Autowired
	protected AppGridPane grid;

	@Autowired
	private AppButton changeButton;

	@Autowired
	private AppCombo<String> fontCombo;

	@Autowired
	private BaseColorPicker colorPicker;

	@Autowired
	private LabelFactory label;

	@Autowired
	private StyleSheet styleSheet;

	@Autowired
	private UserService service;

	private Style style;

	private User user;

	@Override
	public void refresh() {
		colorPicker.reset();
		fontCombo.select(systemFont());
		setFocus();
	}

	@Override
	public void setFocus() {
		colorPicker.requestFocus();
	}

	private Button changeButton() {
		changeButton.large("Change").build();
		changeButton.setOnAction(event -> trySavingChanges());
		return changeButton;
	}

	private String font() {
		return fontCombo.getValue();
	}

	private Node fontCombo() {
		fontCombo.items(Font.getFamilies());
		fontCombo.select(systemFont());
		return fontCombo;
	}

	private AppGridPane grid() {
		grid.getChildren().clear();
		grid.add(label.field("Color"), 0, 0);
		grid.add(colorPicker, 1, 0);
		grid.add(label.field("Font"), 0, 1);
		grid.add(fontCombo(), 1, 1);
		return grid;
	}

	private String rgb() {
		return UI.toRGBA(colorPicker.getValue());
	}

	private void saveChanges() throws Exception {
		updateStyle();
		styleSheet.update(style);
		updateUser();
	}

	private void setStyle() {
		style = user().getStyle();
		if (style == null)
			style = new Style();
	}

	private String systemFont() {
		return Font.getDefault().getFamily();
	}

	private void trySavingChanges() {
		try {
			saveChanges();
			close();
		} catch (Exception e) {
			dialog.show(e).addParent(this).start();
		}
	}

	private void updateStyle() {
		setStyle();
		style.setBase(rgb());
		style.setFont(font());
	}

	private void updateUser() throws Exception {
		user.setStyle(style);
		user = service.save(user);
		Spring.updateUser(user);
	}

	private User user() {
		return user = Spring.user();
	}

	@Override
	protected Button[] buttons() {
		return new Button[] { changeButton(), closeButton() };
	}

	@Override
	protected String headerText() {
		return "Change UI Theme";
	}

	@Override
	protected List<Node> nodes() {
		return Arrays.asList(header(), grid(), buttonBox());
	}
}