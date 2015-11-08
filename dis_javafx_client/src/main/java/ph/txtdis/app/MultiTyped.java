package ph.txtdis.app;

import ph.txtdis.dto.Typed;
import ph.txtdis.type.ModuleType;

public interface MultiTyped extends Typed {

	Startable type(ModuleType type);
}
