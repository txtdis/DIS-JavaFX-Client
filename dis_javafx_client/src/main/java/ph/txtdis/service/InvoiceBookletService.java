package ph.txtdis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.dto.User;
import ph.txtdis.exception.InvoiceIdInBookletAlreadyIssuedException;

@Component
public class InvoiceBookletService implements Listed<InvoiceBooklet>, SavedByEntity<InvoiceBooklet> {

	@Autowired
	private ReadOnlyService<InvoiceBooklet> readOnlyService;

	@Autowired
	private SavingService<InvoiceBooklet> savingService;

	@Autowired
	private UserService userService;

	public void checkForDuplicates(String prefix, Long id, String suffix) throws Exception {
		InvoiceBooklet booklet = find(prefix, id, suffix);
		if (booklet != null)
			throw new InvoiceIdInBookletAlreadyIssuedException(prefix + id + suffix, booklet);
	}

	public InvoiceBooklet find(String prefix, Long id, String suffix) throws Exception {
		return readOnlyService.module(getModule()).getOne("/find?prefix=" + prefix + "&id=" + id + "&suffix=" + suffix);
	}

	@Override
	public String getModule() {
		return "invoiceBooklet";
	}

	@Override
	public List<InvoiceBooklet> list() throws Exception {
		return readOnlyService.module(getModule()).getList();
	}

	public List<User> listUsers() throws Exception {
		return userService.list();
	}

	@Override
	public InvoiceBooklet save(InvoiceBooklet entity) throws Exception {
		return savingService.module(getModule()).save(entity);
	}

	public InvoiceBooklet save(String prefix, String suffix, Long start, Long end, User issuedTo) throws Exception {
		InvoiceBooklet ib = new InvoiceBooklet();
		ib.setPrefix(prefix);
		ib.setSuffix(suffix);
		ib.setStartId(start);
		ib.setEndId(end);
		ib.setIssuedTo(issuedTo);
		return save(ib);
	}
}
