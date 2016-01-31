package ph.txtdis.service;

import static ph.txtdis.type.UserType.AUDITOR;
import static ph.txtdis.type.UserType.FINANCE;
import static ph.txtdis.type.UserType.MANAGER;
import static ph.txtdis.type.UserType.STORE_KEEPER;
import static ph.txtdis.util.SpringUtil.isUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Item;
import ph.txtdis.dto.Keyed;
import ph.txtdis.dto.StockTake;
import ph.txtdis.dto.StockTakeDetail;
import ph.txtdis.type.QualityType;
import ph.txtdis.type.UomType;

@Service("stockTakeService")
public class StockTakeService implements ItemBased<StockTakeDetail>, Reset, Serviced<Long>, SpunById<Long> {

	@Autowired
	private ItemService itemService;

	@Autowired
	private ReadOnlyService<StockTake> readOnlyService;

	@Autowired
	private SavingService<StockTake> savingService;

	@Autowired
	private SpunService<StockTake, Long> spunService;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private UserService userService;

	private List<String> warehouses, takers, checkers;

	private StockTake stockTake;

	private Item item;

	public StockTakeService() {
		reset();
	}

	public StockTakeDetail createDetail(UomType uom, BigDecimal qty, QualityType qc) {
		StockTakeDetail d = new StockTakeDetail();
		d.setId(item.getId());
		d.setName(item.getName());
		d.setQty(qty);
		d.setQuality(qc);
		d.setUom(uom);
		return d;
	}

	@Override
	@SuppressWarnings("unchecked")
	public StockTake get() {
		if (stockTake == null)
			reset();
		return stockTake;
	}

	@Override
	public String getAlternateName() {
		return "Pick List";
	}

	public StockTake getByBooking(Long id) throws Exception {
		return readOnlyService.module(getModule()).getOne("/booking?id=" + id);
	}

	public LocalDate getCountDate() {
		if (get().getCountDate() == null)
			get().setCountDate(LocalDate.now());
		return get().getCountDate();
	}

	@Override
	public String getCreatedBy() {
		return stockTake.getCreatedBy();
	}

	@Override
	public ZonedDateTime getCreatedOn() {
		return stockTake.getCreatedOn();
	}

	@Override
	public List<StockTakeDetail> getDetails() {
		return get().getDetails();
	}

	@Override
	public Long getId() {
		return get().getId();
	}

	@Override
	public Item getItem() {
		return item;
	}

	@Override
	public ItemService getItemService() {
		return itemService;
	}

	@Override
	public String getModule() {
		return "stockTake";
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReadOnlyService<StockTake> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SavingService<StockTake> getSavingService() {
		return savingService;
	}

	@Override
	public SpunService<StockTake, Long> getSpunService() {
		return spunService;
	}

	public boolean isUserAnAuditor() {
		return isUser(AUDITOR) || isUser(FINANCE) || isUser(MANAGER);
	}

	public boolean isCountToday() {
		return getCountDate().equals(LocalDate.now());
	}

	public List<String> listCheckers() {
		return isNew() ? allCheckers() : getCheckers();
	}

	public List<String> listTakers() {
		return isNew() ? allTakers() : getTakers();
	}

	public List<String> listWarehouses() {
		return isNew() ? allWarehouses() : getWarehouses();
	}

	@Override
	public void next() throws Exception {
		set(spunService.module(getModule()).next(getSpunId()));
	}

	@Override
	public void previous() throws Exception {
		set(spunService.module(getModule()).previous(getSpunId()));
	}

	@Override
	public void reset() {
		set(new StockTake());
		instantiateLists();
	}

	@Override
	public <T extends Keyed<Long>> void set(T t) {
		stockTake = (StockTake) t;
	}

	public void setItemUponValidation(Long id) throws Exception {
		item = null;
		item = verifyItem(id);
	}

	private List<String> allCheckers() {
		if (checkers == null)
			checkers = userService.listNamesByRole(AUDITOR, FINANCE);
		return checkers;
	}

	private List<String> allTakers() {
		if (takers == null)
			takers = userService.listNamesByRole(STORE_KEEPER);
		return takers;
	}

	private List<String> allWarehouses() {
		if (warehouses == null)
			warehouses = warehouseService.listNames();
		return warehouses;
	}

	private List<String> getCheckers() {
		String s = get().getChecker();
		return s == null ? null : Arrays.asList(s);
	}

	private List<String> getTakers() {
		String s = get().getTaker();
		return s == null ? null : Arrays.asList(s);
	}

	private List<String> getWarehouses() {
		String s = get().getWarehouse();
		return s == null ? null : Arrays.asList(s);
	}

	private void instantiateLists() {
		takers = null;
		warehouses = null;
		checkers = null;
	}
}
