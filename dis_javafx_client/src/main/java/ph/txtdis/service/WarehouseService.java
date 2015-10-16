package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.exception.DuplicateException;

@Service("warehouseService")
public class WarehouseService implements Listed<Warehouse>, SavedByName<Warehouse>, UniquelyNamed {

	@Autowired
	private SavingService<Warehouse> savingService;

	@Autowired
	private ReadOnlyService<Warehouse> readOnlyService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(getModule()).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	@Override
	public String getModule() {
		return "warehouse";
	}

	@Override
	public List<Warehouse> list() throws Exception {
		return readOnlyService.module(getModule()).getList();
	}

	@Override
	public Warehouse save(String name) throws Exception {
		Warehouse entity = new Warehouse();
		entity.setName(name);
		return savingService.module(getModule()).save(entity);
	}
}
