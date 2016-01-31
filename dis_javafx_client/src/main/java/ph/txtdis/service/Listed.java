package ph.txtdis.service;

import static java.util.Collections.emptyList;

import java.util.List;

public interface Listed<T> extends Moduled {

	ReadOnlyService<T> getReadOnlyService();

	default List<T> list() {
		try {
			return getReadOnlyService().module(getModule()).getList();
		} catch (Exception e) {
			e.printStackTrace();
			return emptyList();
		}
	}
}
