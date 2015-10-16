package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Truck;
import ph.txtdis.exception.DuplicateException;

@Service
public class TruckService implements Listed<Truck>, SavedByName<Truck>, UniquelyNamed {

	@Autowired
	private ReadOnlyService<Truck> readOnlyService;

	@Autowired
	private SavingService<Truck> savingService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(getModule()).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	@Override
	public String getModule() {
		return "truck";
	}

	@Override
	public List<Truck> list() throws Exception {
		return readOnlyService.module(getModule()).getList();
	}

	@Override
	public Truck save(String name) throws Exception {
		Truck entity = new Truck();
		entity.setName(name);
		return savingService.module(getModule()).save(entity);
	}
}
