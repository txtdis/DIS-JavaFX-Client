package ph.txtdis.fx.table;

import static javafx.collections.FXCollections.emptyObservableList;
import static javafx.collections.FXCollections.observableArrayList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import ph.txtdis.excel.Tabular;

@Getter
@Setter
public abstract class AppTable<S> extends TableView<S> implements Tabular {

	private S item;

	public AppTable() {
		setStyle("-fx-opacity: 1; ");
	}

	public AppTable<S> build() {
		addColumns();
		setMinWidth(width());
		addProperties();
		return this;
	}

	public void disableIf(BooleanBinding b) {
		disableProperty().bind(b);
	}

	@Override
	public int getColumnCount() {
		return getColumns().size();
	}

	@Override
	public int getColumnIndexOfFirstTotal() {
		return getColumnCount() - getColumnTotals().size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getColumnTotals() {
		return getUserData() == null ? new ArrayList<>() : (List<BigDecimal>) getUserData();
	}

	@Override
	public int getLastRowIndex() {
		return getItems().size();
	}

	public BooleanBinding isEmpty() {
		return itemsProperty().isEqualTo(emptyObservableList());
	}

	@SuppressWarnings("unchecked")
	public void items(List<?> list) {
		setItems(list == null ? emptyObservableList() : (ObservableList<S>) observableArrayList(list));
	}

	public void setOnItemChange(InvalidationListener listener) {
		itemsProperty().addListener(listener);
	}

	protected abstract void addColumns();

	protected void addProperties() {
	}

	protected double width() {
		double width = 20;
		for (TableColumn<S, ?> column : getColumns()) {
			if (column.isVisible())
				width = width + column.getMinWidth();
		}
		return width;
	}
}
