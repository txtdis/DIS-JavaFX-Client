package ph.txtdis.fx;

import ph.txtdis.app.Startable;

public interface AppSelectable<T> extends Startable {

	T getSelection();
}
