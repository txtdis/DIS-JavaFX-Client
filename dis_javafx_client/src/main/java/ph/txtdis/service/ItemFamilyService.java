package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ph.txtdis.type.ItemTier.PRODUCT;

import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;
import ph.txtdis.type.ItemTier;

@Service("itemFamilyService")
public class ItemFamilyService implements Listed<ItemFamily>, SavedByName<ItemFamily>, UniquelyNamed<ItemFamily> {

	@Autowired
	private ReadOnlyService<ItemFamily> readOnlyService;

	@Autowired
	private SavingService<ItemFamily> savingService;

	public List<ItemFamily> getItemAncestry(Item item) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getList("/ancestry?family=" + item.getFamily().getId());
	}

	@Override
	public String getModule() {
		return "itemFamily";
	}

	@Override
	public ReadOnlyService<ItemFamily> getReadOnlyService() {
		return readOnlyService;
	}

	public List<ItemFamily> listItemFamily(ItemTier t) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getList("/perTier?tier=" + t);
	}

	public List<ItemFamily> listItemParents() throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getList("/parents");
	}

	@Override
	public ItemFamily save(String name) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		return save(name, PRODUCT);
	}

	public ItemFamily save(String name, ItemTier tier) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException {
		ItemFamily entity = new ItemFamily();
		entity.setName(name);
		entity.setTier(tier);
		return savingService.module(getModule()).save(entity);
	}
}
