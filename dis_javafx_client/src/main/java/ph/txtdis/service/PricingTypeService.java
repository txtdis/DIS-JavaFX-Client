package ph.txtdis.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.PricingType;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.StoppedServerException;

@Service("pricingTypeService")
public class PricingTypeService
		implements Iconed, Listed<PricingType>, SavedByName<PricingType>, UniquelyNamed<PricingType>
{

	@Autowired
	private ReadOnlyService<PricingType> readOnlyService;

	@Autowired
	private SavingService<PricingType> savingService;

	@Override
	public String getHeaderText() {
		return "Pricing Type";
	}

	@Override
	public String getModule() {
		return "pricingType";
	}

	@Override
	public ReadOnlyService<PricingType> getReadOnlyService() {
		return readOnlyService;
	}

	public String getTitleText() {
		return getHeaderText();
	}

	public List<String> listNames() {
		try {
			return list().stream().map(r -> r.getName()).sorted().collect(toList());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public PricingType save(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		PricingType entity = new PricingType();
		entity.setName(name);
		return savingService.module(getModule()).save(entity);
	}
}
