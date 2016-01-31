package ph.txtdis.fx.tab;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.PHONE;
import static ph.txtdis.type.Type.TEXT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ph.txtdis.dto.Customer;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.table.CreditTable;
import ph.txtdis.service.CustomerService;

@Scope("prototype")
@Component("creditTab")
public class CreditTab extends AbstractTab {

	@Autowired
	private CustomerService service;

	@Autowired
	private LabelFactory label;

	@Autowired
	private AppField<String> contactNameField, contactSurnameField, titleField, mobileField;

	@Autowired
	private CreditTable creditTable;

	public CreditTab() {
		super("Credit Details");
	}

	@Override
	public void refresh() {
		contactNameField.setText(customer().getContactName());
		contactSurnameField.setText(customer().getContactSurname());
		titleField.setText(customer().getContactTitle());
		mobileField.setValue(customer().getMobile());
		creditTable.items(customer().getCreditDetails());
	}

	@Override
	public void save() {
		customer().setContactName(contactNameField.getText());
		customer().setContactSurname(contactSurnameField.getText());
		customer().setContactTitle(titleField.getText());
		customer().setCreditDetails(creditTable.getItems());
	}

	private Customer customer() {
		return service.get();
	}

	private VBox tablePane() {
		return box.forVerticals(label.group("Approved Credit History"), creditTable.build());
	}

	private void validateMobileNo() {
		String ph = mobileField.getValue();
		if (!ph.isEmpty())
			try {
				service.validatePhoneNo(ph);
			} catch (Exception e) {
				mobileField.clear();
				handleError(mobileField, e);
			}
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
		return asList(gridPane, box.forHorizontalPane(tablePane()));
	}

	@Override
	protected void setBindings() {
		contactSurnameField.disableIf(contactNameField.isEmpty());
		titleField.disableIf(contactSurnameField.isEmpty());
		mobileField.disableIf(titleField.isEmpty());
		creditTable.disableIf(mobileField.isEmpty());
	}

	@Override
	protected void setListeners() {
		super.setListeners();
		mobileField.setOnAction(e -> validateMobileNo());
	}
}
