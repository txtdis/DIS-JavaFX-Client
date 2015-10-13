package ph.txtdis.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ph.txtdis.dto.Account;
import ph.txtdis.dto.Customer;
import ph.txtdis.dto.Route;
import ph.txtdis.exception.DuplicateException;

@Service
public class RouteService implements Listed<Route>, UniquelyNamed, SavedByName<Route> {

	private static final String AGING_RECEIVABLE = "agingReceivable";

	private static final String ROUTE = "route";

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SavingService<Route> savingService;

	@Autowired
	private ReadOnlyService<Route> readOnlyService;

	@Autowired
	private UserService userService;

	private Route route;

	@Override
	public void confirmUniqueness(String name) throws Exception {
		if (readOnlyService.module(ROUTE).getOne("/" + name) != null)
			throw new DuplicateException(name);
	}

	public Route find(String id) throws Exception {
		return route = readOnlyService.module(ROUTE).getOne("/find?id=" + id);
	}

	public Route find(String[] ids) throws Exception {
		return launcedFromAgingReceivable(ids) ? viaCustomer(ids) : find(routeId(ids));
	}

	public Route getRoute() {
		return route;
	}

	public List<Account> getSellerHistory() {
		return route == null || route.getSellerHistory() == null ? new ArrayList<>() : route.getSellerHistory();
	}

	@Override
	public List<Route> list() throws Exception {
		return readOnlyService.module(ROUTE).getList();
	}

	public List<String> listUsers() throws Exception {
		return userService.list().stream().map(u -> u.getUsername()).collect(Collectors.toList());
	}

	@Override
	public Route save(String name) throws Exception {
		Route entity = new Route();
		entity.setName(name);
		return savingService.module(ROUTE).save(entity);
	}

	public Account save(String seller, LocalDate startDate) throws Exception {
		List<Account> list = updatedSellerHistory(seller, startDate);
		route.setSellerHistory(list);
		route = savingService.module(ROUTE).save(route);
		return getSellerHistory().stream()
				.filter(s -> s.getSeller().equals(seller) && s.getStartDate().equals(startDate)).findAny().get();
	}

	private Account createAccount(String seller, LocalDate startDate) {
		Account account = new Account();
		account.setSeller(seller);
		account.setStartDate(startDate);
		return account;
	}

	private boolean launcedFromAgingReceivable(String[] ids) {
		return ids[2] == null ? false : ids[2].equals(AGING_RECEIVABLE);
	}

	private String routeId(String[] ids) {
		return ids[0];
	}

	private List<Account> updatedSellerHistory(String seller, LocalDate startDate) {
		List<Account> list = getSellerHistory();
		list.add(createAccount(seller, startDate));
		return list;
	}

	private Route viaCustomer(String[] ids) throws Exception {
		Customer c = customerService.find(ids[0]);
		return route = c.getRoute(LocalDate.now());
	}
}
