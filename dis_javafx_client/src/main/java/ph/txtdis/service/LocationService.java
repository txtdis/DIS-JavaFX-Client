package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Location;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Service
public class LocationService {

	@Autowired
	private ReadOnlyService<Location> readOnlyService;

	public List<Location> listBarangays(Location city) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return city == null ? null : readOnlyService.module("location").getList("/barangays?of=" + city.getId());
	}

	public List<Location> listCities(Location province) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return province == null ? null : readOnlyService.module("location").getList("/cities?of=" + province.getId());
	}

	public List<Location> listProvinces() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module("location").getList("/provinces");
	}
}
