package ph.txtdis.fx.dialog;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import ph.txtdis.fx.control.AppButton;
import ph.txtdis.fx.control.InputNode;
import ph.txtdis.fx.pane.AppGridPane;

public abstract class FieldDialog<T> extends InputDialog implements Inputted<T> {

	@Autowired
	protected AppGridPane grid;

	@Autowired
	protected AppButton addButton;

	protected T entity;

	protected List<InputNode<?>> inputNodes;

	@Override
	public T getAddedItem() {
		return entity;
	}

	@Override
	public void refresh() {
		inputNodes.forEach(inputNode -> inputNode.reset());
		super.refresh();
	}

	@Override
	public void setFocus() {
		inputNodes.get(0).requestFocus();
	}

	private AppGridPane grid() {
		grid.getChildren().clear();
		populateGrid();
		return grid;
	}

	private void populateGrid() {
		inputNodes = addNodes();
		if (inputNodes != null)
			putNodes();
	}

	private void putLabeledControls(int row) {
		List<Node> nodes = inputNodes.get(row).getNodes();
		for (int column = 0; column < nodes.size(); column++)
			grid.add(nodes.get(column), column, row);
	}

	private void putNodes() {
		for (int row = 0; row < inputNodes.size(); row++)
			putLabeledControls(row);
	}

	protected Button addButton() {
		addButton.large("Add").build();
		addButton.setOnAction(event -> addItem());
		addButton.disableIf(getAddButtonDisableBindings());
		return addButton;
	}

	protected void addItem() {
		entity = createEntity();
		refresh();
		close();
	}

	protected abstract List<InputNode<?>> addNodes();

	@Override
	protected Button[] buttons() {
		return new Button[] { addButton(), closeButton() };
	}

	protected abstract T createEntity();

	protected BooleanBinding getAddButtonDisableBindings() {
		BooleanBinding binding = inputNodes.get(0).isEmpty();
		for (int i = 1; i < inputNodes.size(); i++)
			binding = binding.or(inputNodes.get(i).isEmpty());
		return binding;
	}

	@Override
	protected String headerText() {
		return "Add New " + super.headerText();
	}

	@Override
	protected List<Node> nodes() {
		return asList(header(), grid(), buttonBox());
	}

	protected void resetNodesOnError(Exception e) {
		e.printStackTrace();
		dialog.show(e).addParent(this).start();
		refresh();
	}

	@Override
	protected void setOnFiredCloseButton() {
		entity = null;
		refresh();
		super.setOnFiredCloseButton();
	}
}
