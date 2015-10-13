package ph.txtdis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import ph.txtdis.dto.Receiving;
import ph.txtdis.exception.NotFoundException;

@Service
@NoArgsConstructor
public class ReceivingService implements AlternateNamed {

	@Autowired
	private ReadOnlyService<Receiving> readOnlyService;

	public Receiving find(Long id) throws Exception {
		Receiving e = readOnlyService.module("receiving").getOne("/" + id);
		if (e == null)
			throw new NotFoundException("ID No. " + id);
		return e;
	}

	@Override
	public String getAlternateName() {
		return "R/R";
	}

	protected Receiving getByBooking(Long id) throws Exception {
		return readOnlyService.module("receiving").getOne("/booking?id=" + id);
	}
}
