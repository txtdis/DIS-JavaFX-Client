package ph.txtdis.fx.table;

import static javafx.collections.FXCollections.emptyObservableList;
import static javafx.collections.FXCollections.observableArrayList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.TextAlignment;
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

	@Override
	@SuppressWarnings("unchecked")
	public void items(List<?> list) {
		setItems(list == null ? emptyObservableList() : (ObservableList<S>) observableArrayList(list));
	}

	public void setOnEmpty(String text) {
		setPlaceholder(message(text));
	}

	public void setOnItemChange(InvalidationListener listener) {
		itemsProperty().addListener(listener);
	}

	private Label message(String text) {
		Label label = new Label(text);
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setPrefHeight(75);
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		return label;
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
