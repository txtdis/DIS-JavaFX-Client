package ph.txtdis.app;

import static java.util.Arrays.asList;
import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.fx.dialog.AuditDialog;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.info.SuccessfulSaveInfo;
import ph.txtdis.service.DecisionNeeded;
import ph.txtdis.service.NeededDecisionDisplayed;
import ph.txtdis.util.TypeMap;

@Scope("prototype")
@Component("decisionNeededApp")
public class DecisionNeededApp {

	private static final String BASE = "-fx-text-base-color; ";

	private static final String BRIGHT_RED = "#ff0000; ";

	private static final String BRIGHT_GREEN = "#00ff00; ";

	@Autowired
	private AppButton decisionButton;

	@Autowired
	private AuditDialog decisionDialog;

	@Autowired
	private AppField<String> decidedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> decidedOnDisplay;

	@Autowired
	private LabelFactory label;

	@Autowired
	private MessageDialog dialog;

	@Autowired
	private TypeMap map;

	private BooleanProperty canBeApproved = new SimpleBooleanProperty(false);

	private EventHandler<ActionEvent> event;

	private List<Node> decisionDisplays;

	public List<Node> addApprovalNodes() {
		return decisionNodes("Dis/approved");
	}

	public List<Node> addAuditNodes() {
		return decisionNodes("Audited");
	}

	public AppButton addDecisionButton() {
		return decisionButton.icon("decision").tooltip("Decide...").build();
	}

	public void hideDecisionNodesIf(BooleanBinding b) {
		if (decisionDisplays != null) {
			decisionDisplays.forEach(n -> n.managedProperty().bind(b));
			decisionButton.managedProperty().bind(b);
		}
	}

	public BooleanBinding isAudited() {
		try {
			return decidedOnDisplay.isNotEmpty();
		} catch (Exception e) {
			return null;
		}
	}

	public void refresh(NeededDecisionDisplayed service) {
		canBeApproved.set(service.canApprove());
		refreshDisplays(service);
		refreshButton(service.getIsValid());
	}

	public void setDecisionButtonOnAction(EventHandler<ActionEvent> e) {
		if (decisionButton != null)
			decisionButton.setOnAction(event = e);
	}

	public void showDecisionDialogForValidation(Stage s, DecisionNeeded a) {
		if (decisionDialog == null)
			return;
		canBeApproved.set(a.canApprove());
		decisionDialog(s, a);
	}

	private void decisionDialog(Stage s, DecisionNeeded a) {
		decisionDialog.disableApprovalButtonIf(canBeApproved.not())//
				.addParent(s).start();
		if (decisionDialog.isValid() != null)
			saveDecision(s, a);
		if (a.closeAppIfInvalid())
			s.close();
	}

	private List<Node> decisionNodes(String s) {
		return decisionDisplays = asList(label.name(s + " by"), //
				decidedByDisplay.readOnly().width(120).build(TEXT), label.name("on"), //
				decidedOnDisplay.readOnly().build(TIMESTAMP));
	}

	private void refreshButton(Boolean isValid) {
		if (isValid == null)
			updateButton("decision", BASE, "Decide...");
		else if (isValid)
			updateButton("accept", BRIGHT_GREEN, "Valid");
		else
			updateButton("reject", BRIGHT_RED, "Invalid");
	}

	private void refreshDisplays(NeededDecisionDisplayed service) {
		if (decidedByDisplay == null)
			return;
		decidedByDisplay.setValue(service.getDecidedBy());
		decidedOnDisplay.setValue(service.getDecidedOn());
	}

	private void saveDecision(DecisionNeeded a) throws Exception, SuccessfulSaveInfo {
		Boolean isValid = decisionDialog.isValid();
		String remarks = decisionDialog.getFindings();
		a.updatePerValidity(isValid, remarks);
		a.saveDecision();
	}

	private void saveDecision(Stage s, DecisionNeeded a) {
		try {
			saveDecision(a);
		} catch (SuccessfulSaveInfo i) {
			dialog.show(i).addParent(s).start();
		} catch (Exception e) {
			e.printStackTrace();
			dialog.show(e).addParent(s).start();
		}
	}

	private void setButtonStyle(String c) {
		String s = decisionButton.getStyle();
		decisionButton.setStyle(s + " -fx-text-fill: " + c);
	}

	private void updateButton(String n, String c, String tt) {
		decisionButton.setText(map.icon(n));
		decisionButton.getTooltip().setText(tt);
		decisionButton.setOnAction(c.equals(BASE) ? event : null);
		setButtonStyle(c);
	}
}
