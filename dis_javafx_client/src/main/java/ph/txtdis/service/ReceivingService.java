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
		Receiving rr = readOnlyService.module("receiving").getOne("/" + id);
		if (rr == null)
			throw new NotFoundException("R/R No. " + id);
		return rr;
	}

	@Override
	public String getAlternateName() {
		return "R/R";
	}

	protected Receiving findByBooking(Long id) throws Exception {
		Receiving rr = readOnlyService.module("receiving").getOne("/booking?id=" + id);
		if (rr == null)
			throw new NotFoundException("R/R No. " + id);
		return rr;
	}
}
