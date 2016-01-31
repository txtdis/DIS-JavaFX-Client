package ph.txtdis.service;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import static ph.txtdis.type.ItemTier.PRINCIPAL;

import ph.txtdis.dto.ItemFamily;

public interface ItemFamilyLimited {

	default ItemFamily getItemFamilyForAll() {
		ItemFamily a = new ItemFamily();
		a.setName("ALL");
		a.setTier(PRINCIPAL);
		return a;
	}

	ItemFamilyService getItemFamilyService();

	default List<ItemFamily> listAllFamilies() {
		try {
			List<ItemFamily> l = new ArrayList<>(asList(getItemFamilyForAll()));
			l.addAll(getItemFamilyService().list());
			return l;
		} catch (Exception e) {
			return null;
		}
	}

	default ItemFamily nullIfAll(ItemFamily f) {
		return f.equals(getItemFamilyForAll()) ? null : f;
	}
}
