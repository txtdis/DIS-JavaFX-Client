package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Warehouse;
import ph.txtdis.exception.DuplicateException;

@Service("warehouseService")
public class WarehouseService implements Listed<Warehouse>, SavedByName<Warehouse>, UniquelyNamed {

	private static final String WAREHOUSE = "warehouse";

	@Autowired
	private SavingService<Warehouse> savingService;

	@Autowired
	private ReadOnlyService<Warehouse> readOnlyService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(WAREHOUSE).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	@Override
	public List<Warehouse> list() throws Exception {
		return readOnlyService.module(WAREHOUSE).getList();
	}

	@Override
	public Warehouse save(String name) throws Exception {
		Warehouse entity = new Warehouse();
		entity.setName(name);
		return savingService.module(WAREHOUSE).save(entity);
	}
}
