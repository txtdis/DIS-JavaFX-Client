package ph.txtdis.service;

public interface AlternateNamed {

	String getAlternateName();

	default String getModuleId() {
		return getAlternateName() + " No. ";
	}
}
