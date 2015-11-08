package ph.txtdis.fx.control;

import java.util.List;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;

public interface InputNode<T> {

	List<Node> getNodes();

	T getValue();

	BooleanBinding isEmpty();

	void requestFocus();

	void reset();
}