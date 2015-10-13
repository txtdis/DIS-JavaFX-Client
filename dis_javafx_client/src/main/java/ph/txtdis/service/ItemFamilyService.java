package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Item;
import ph.txtdis.dto.ItemFamily;
import ph.txtdis.exception.DuplicateException;
import ph.txtdis.type.ItemTier;

@Service("itemFamilyService")
public class ItemFamilyService implements Listed<ItemFamily>, SavedByName<ItemFamily>, UniquelyNamed {

	private static final String ITEM_FAMILY = "itemFamily";

	@Autowired
	private ReadOnlyService<ItemFamily> readOnlyService;

	@Autowired
	private SavingService<ItemFamily> savingService;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(ITEM_FAMILY).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	public List<ItemFamily> getItemAncestry(Item item) throws Exception {
		return readOnlyService.module(ITEM_FAMILY).getList("ancestry?family=" + item.getFamily().getId());
	}

	@Override
	public List<ItemFamily> list() throws Exception {
		return readOnlyService.module(ITEM_FAMILY).getList();
	}

	@Override
	public ItemFamily save(String name) throws Exception {
		return save(name, ItemTier.PRODUCT);
	}

	public ItemFamily save(String name, ItemTier tier) throws Exception {
		ItemFamily entity = new ItemFamily();
		entity.setName(name);
		entity.setTier(tier);
		return savingService.module(ITEM_FAMILY).save(entity);
	}
}
