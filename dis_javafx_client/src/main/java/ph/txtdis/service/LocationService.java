package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Location;

@Service
public class LocationService {

	@Autowired
	private ReadOnlyService<Location> readOnlyService;

	public List<Location> listBarangays(Location city) throws Exception {
		return city == null ? null : readOnlyService.module("location").getList("/barangays?of=" + city.getId());
	}

	public List<Location> listCities(Location province) throws Exception {
		return province == null ? null : readOnlyService.module("location").getList("/cities?of=" + province.getId());
	}

	public List<Location> listProvinces() throws Exception {
		return readOnlyService.module("location").getList("/provinces");
	}
}
