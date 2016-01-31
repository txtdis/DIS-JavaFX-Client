package ph.txtdis.service;

import static org.apache.commons.lang3.StringUtils.capitalize;

public interface Moduled extends Iconed {

	default String getHeaderText() {
		return capitalize(getModule());
	}

	default String getOpenDialogHeading() {
		return "Open a(n) " + getHeaderText();
	}
}
