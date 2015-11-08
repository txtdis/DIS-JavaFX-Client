package ph.txtdis.dto;

public interface Remarked<PK> extends Keyed<PK> {

	String getRemarks();

	void setRemarks(String s);
}
