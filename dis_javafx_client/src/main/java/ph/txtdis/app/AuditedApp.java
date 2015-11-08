package ph.txtdis.app;

import static ph.txtdis.type.Type.TEXT;
import static ph.txtdis.type.Type.TIMESTAMP;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.AppField;
import ph.txtdis.fx.control.LabelFactory;
import ph.txtdis.service.Audited;
import ph.txtdis.util.TypeMap;

@Scope("prototype")
@Component("auditedApp")
public class AuditedApp {

	private static final String BASE = "-fx-text-base-color; ";

	private static final String BRIGHT_RED = "#ff0000; ";

	private static final String BRIGHT_GREEN = "#00ff00; ";

	@Autowired
	private AppButton auditButton;

	@Autowired
	private AppField<String> auditedByDisplay;

	@Autowired
	private AppField<ZonedDateTime> auditedOnDisplay;

	@Autowired
	private LabelFactory label;

	@Autowired
	private TypeMap map;

	private EventHandler<ActionEvent> event;

	private List<Node> auditDisplays;

	public AppButton addAuditButton() {
		return auditButton.icon("audit").tooltip("Enter audit\nfindings...").build();
	}

	public HBox addAuditDisplays(HBox b) {
		// @formatter:on
		List<Node> n = new ArrayList<>(b.getChildren());
		n.addAll(auditDisplays = Arrays.asList(label.name("Audited by"),
				auditedByDisplay.readOnly().width(120).build(TEXT), label.name("on"),
				auditedOnDisplay.readOnly().build(TIMESTAMP)));
		b.getChildren().setAll(n);
		return b;
		// @formatter:on
	}

	public void hideAuditNodesIf(BooleanProperty b) {
		auditDisplays.forEach(n -> n.managedProperty().bind(b));
		auditButton.managedProperty().bind(b);
	}

	public BooleanBinding isAudited() {
		return auditedOnDisplay.isNotEmpty();
	}

	public void refresh(Audited service) {
		auditedByDisplay.setValue(service.getAuditedBy());
		auditedOnDisplay.setValue(service.getAuditedOn());
		refreshButton(service.getIsValid());
	}

	public void setOnAuditButtonClick(EventHandler<ActionEvent> e) {
		auditButton.setOnAction(event = e);
	}

	private void refreshButton(Boolean isValid) {
		if (isValid == null)
			updateButton("audit", BASE, "Enter audit\nfindings...");
		else if (isValid)
			updateButton("accept", BRIGHT_GREEN, "Valid");
		else
			updateButton("reject", BRIGHT_RED, "Invalid");
	}

	private void setButtonStyle(String c) {
		String s = auditButton.getStyle();
		auditButton.setStyle(s + " -fx-text-fill: " + c);
	}

	private void updateButton(String n, String c, String tt) {
		auditButton.setText(map.icon(n));
		auditButton.getTooltip().setText(tt);
		auditButton.setOnAction(c.equals(BASE) ? event : null);
		setButtonStyle(c);
	}
}
