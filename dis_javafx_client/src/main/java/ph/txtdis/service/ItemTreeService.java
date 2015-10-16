package ph.txtdis.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.ItemFamily;
import ph.txtdis.dto.ItemTree;
import ph.txtdis.exception.DuplicateException;

@Service
public class ItemTreeService implements Listed<ItemTree>, SavedByEntity<ItemTree> {

	@Autowired
	private ItemFamilyService familyService;

	@Autowired
	private ReadOnlyService<ItemTree> readOnlyService;

	@Autowired
	private SavingService<ItemTree> savingService;

	public boolean duplicated(ItemFamily family, ItemFamily parent) throws Exception {
		if (readOnlyService.module(getModule())
				.getOne("/find?family=" + family.getId() + "&parent=" + parent.getId()) != null)
			throw new DuplicateException(family + " - " + parent);
		return false;
	}

	@Override
	public String getModule() {
		return "itemTree";
	}

	@Override
	public List<ItemTree> list() throws Exception {
		return readOnlyService.module(getModule()).getList();
	}

	public List<ItemFamily> listFamilies() throws Exception {
		return familyService.list();
	}

	public List<ItemFamily> listParents(ItemFamily family) throws Exception {
		return family == null ? Collections.emptyList()
				: familyService.list().stream().filter(f -> f.getTier().ordinal() > family.getTier().ordinal())
						.collect(Collectors.toList());
	}

	public ItemTree save(ItemFamily family, ItemFamily parent) throws Exception {
		ItemTree it = new ItemTree();
		it.setFamily(family);
		it.setParent(parent);
		return save(it);
	}

	@Override
	public ItemTree save(ItemTree entity) throws Exception {
		return savingService.module(getModule()).save(entity);
	}
}
