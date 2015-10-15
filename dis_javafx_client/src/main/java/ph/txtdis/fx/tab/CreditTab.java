package ph.txtdis.fx.tab;

import static ph.txtdis.type.Type.PHONE;
import static ph.txtdis.type.Type.TEXT;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.table.CreditTable;
import ph.txtdis.service.CustomerService;

@Lazy
@Component("creditTab")
public class CreditTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<String> contactNameField;

	@Autowired
	private AppField<String> contactSurnameField;

	@Autowired
	private AppField<String> titleField;

	@Autowired
	private AppField<String> mobileField;

	@Autowired
	private CreditTable creditTable;

	public CreditTab() {
		super("Credit Details");
	}

	@Override
	public CreditTab build() {
		super.build();
		setBindings();
		return this;
	}

	@Override
	public void refresh() {
		contactNameField.setText(service.getCreditContactName());
		contactSurnameField.setText(service.getCreditContactName());
		titleField.setText(service.getContactTitle());
		mobileField.setValue(service.getMobile());
		creditTable.items(service.getCreditDetails());
	}

	@Override
	public void save() {
		service.setCreditContactName(contactNameField.getText());
		service.setCreditContactSurname(contactSurnameField.getText());
		service.setContactTitle(titleField.getText());
		service.setMobile(mobileField.getValue());
		service.setCreditDetails(creditTable.getItems());
	}

	private void setBindings() {
		contactSurnameField.disableIf(contactNameField.isEmpty());
		titleField.disableIf(contactSurnameField.isEmpty());
		mobileField.disableIf(titleField.isEmpty());
		creditTable.disableIf(mobileField.isEmpty());
	}

	private VBox tablePane() {
		return box.vbox(label.group("Approved Credit History"), creditTable.build());
	}

	@Override
	protected List<Node> mainVerticalPaneNodes() {
		gridPane.getChildren().clear();
		gridPane.add(label.group("Credit Contact"), 0, 0, 3, 1);
		gridPane.add(label.field("Given Name"), 0, 1);
		gridPane.add(contactNameField.build(TEXT), 1, 1);
		gridPane.add(label.field("Surname"), 2, 1);
		gridPane.add(contactSurnameField.build(TEXT), 3, 1);
		gridPane.add(label.field("Designation"), 0, 2);
		gridPane.add(titleField.build(TEXT), 1, 2);
		gridPane.add(label.field("Mobile No."), 2, 2);
		gridPane.add(mobileField.build(PHONE), 3, 2);
		return Arrays.asList(gridPane, box.hpane(tablePane()));
	}
}
