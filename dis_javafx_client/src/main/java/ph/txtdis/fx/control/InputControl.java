package ph.txtdis.fx.control;

public interface InputControl<T> {

	void clear();

	T getValue();

	void setValue(T value);
}
