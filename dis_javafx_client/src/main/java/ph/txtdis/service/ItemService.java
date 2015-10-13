package ph.txtdis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Item;
import ph.txtdis.dto.QtyPerUom;
import ph.txtdis.exception.NotFoundException;
import ph.txtdis.exception.NotSoldItemException;
import ph.txtdis.type.UomType;

@Service("itemService")
public class ItemService {

	@Autowired
	private ReadOnlyService<Item> readOnlyService;

	public Item find(Long id) throws Exception {
		Item e = readOnlyService.module("item").getOne("/" + id);
		if (e == null)
			throw new NotFoundException("Item No. " + id);
		return e;
	}

	public List<UomType> listSellingUoms(Item item) throws NotSoldItemException {
		List<UomType> uom = filterSoldUom(item);
		if (uom.isEmpty())
			throw new NotSoldItemException(item);
		return uom;
	}

	private List<UomType> filterSoldUom(Item item) {
		List<QtyPerUom> qpu = item.getQtyPerUomList();
		return qpu == null ? new ArrayList<>()
				: qpu.stream().filter(q -> q.isSold()).map(q -> q.getUom()).collect(Collectors.toList());
	}
}
