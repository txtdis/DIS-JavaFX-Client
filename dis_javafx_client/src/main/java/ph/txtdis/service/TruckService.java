package ph.txtdis.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Truck;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;

@Service("truckService")
public class TruckService implements Iconed, Listed<Truck>, SavedByName<Truck>, UniquelyNamed<Truck> {

	@Autowired
	private ReadOnlyService<Truck> readOnlyService;

	@Autowired
	private SavingService<Truck> savingService;

	@Override
	public String getModule() {
		return "truck";
	}

	@Override
	public ReadOnlyService<Truck> getReadOnlyService() {
		return readOnlyService;
	}

	public List<String> listNames() {
		try {
			return list().stream().map(r -> r.getName()).sorted().collect(toList());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Truck save(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		Truck t = new Truck();
		t.setName(name);
		return savingService.module(getModule()).save(t);
	}
}
