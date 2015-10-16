package ph.txtdis.service;

import java.util.List;

public interface Listed<T> extends Moduled {

	List<T> list() throws Exception;
}
