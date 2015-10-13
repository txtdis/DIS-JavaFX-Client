package ph.txtdis.service;

public interface SavedByEntity<T> {

	T save(T entity) throws Exception;
}
