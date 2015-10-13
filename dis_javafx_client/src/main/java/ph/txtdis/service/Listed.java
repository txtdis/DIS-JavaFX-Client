package ph.txtdis.service;

import java.util.List;

public interface Listed<T> {

	List<T> list() throws Exception;
}
