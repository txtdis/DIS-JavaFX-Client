package ph.txtdis.fx;

import javafx.stage.Stage;
import ph.txtdis.fx.dialog.MessageDialog;
import ph.txtdis.fx.dialog.SearchDialog;
import ph.txtdis.service.ByNameSearchable;

public interface Searchable<T> {

	AppSelectable<T> getListApp();

	ByNameSearchable<T> getSearchableByNameService();

	default T getSelectionFromSearchResults(Stage s) {
		getListApp().addParent(s).start();
		return getListApp().getSelection();
	}

	void nextFocus();

	default void openSearchDialog(Stage s, MessageDialog md, SearchDialog sd) {
		refresh();
		sd.criteria("name").addParent(s).start();
		String t = sd.getText();
		if (t != null)
			search(t, s, md);
	}

	void refresh();

	default void search(String name, Stage s, MessageDialog d) {
		try {
			getSearchableByNameService().search(name);
			validateSelectionFromSearchList(s);
			nextFocus();
		} catch (Exception e) {
			d.show(e).addParent(s).start();
		}
	}

	void updateUponVerification(T t);

	default void validateSelectionFromSearchList(Stage s) {
		T t = getSelectionFromSearchResults(s);
		if (t != null)
			updateUponVerification(t);
	}
}
