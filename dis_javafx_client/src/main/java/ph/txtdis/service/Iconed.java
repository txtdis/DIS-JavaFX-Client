package ph.txtdis.service;

import ph.txtdis.util.TypeMap;

public interface Iconed {

	default String getFontIcon() {
		return new TypeMap().icon(getModule());
	}

	String getModule();
}
