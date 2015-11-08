package ph.txtdis.service;

import org.apache.commons.lang3.StringUtils;

public interface Moduled {

	default String getHeaderText() {
		return StringUtils.capitalize(getModule());
	}

	String getModule();

	default String getOpenDialogHeading() {
		return "Open a(n) " + getHeaderText();
	}
}
