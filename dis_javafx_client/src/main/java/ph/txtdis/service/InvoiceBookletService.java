package ph.txtdis.service;

import static ph.txtdis.type.UserType.DRIVER;
import static ph.txtdis.type.UserType.SELLER;
import static ph.txtdis.util.TextUtils.nullIfEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ph.txtdis.dto.InvoiceBooklet;
import ph.txtdis.exception.FailedAuthenticationException;
import ph.txtdis.exception.InvalidException;
import ph.txtdis.exception.InvoiceIdInBookletAlreadyIssuedException;
import ph.txtdis.exception.NoServerConnectionException;
import ph.txtdis.exception.RestException;
import ph.txtdis.exception.StoppedServerException;

@Component
public class InvoiceBookletService implements Iconed, Listed<InvoiceBooklet>, SavedByEntity<InvoiceBooklet> {

	@Autowired
	private ReadOnlyService<InvoiceBooklet> readOnlyService;

	@Autowired
	private SavingService<InvoiceBooklet> savingService;

	@Autowired
	private UserService userService;

	public void checkForDuplicates(String prefix, Long id, String suffix)
			throws NoServerConnectionException, StoppedServerException, FailedAuthenticationException, InvalidException,
			InvoiceIdInBookletAlreadyIssuedException, RestException {
		InvoiceBooklet b = find(nullIfEmpty(prefix), id, nullIfEmpty(suffix));
		if (b != null)
			throw new InvoiceIdInBookletAlreadyIssuedException(addHyphenIfNotEmpty(prefix) + id + suffix, b);
	}

	public InvoiceBooklet find(String p, Long id, String s) throws NoServerConnectionException, StoppedServerException,
			FailedAuthenticationException, InvalidException, RestException {
		return readOnlyService.module(getModule()).getOne("/find?prefix=" + p + "&id=" + id + "&suffix=" + s);
	}

	public int getLinesPerPage() {
		try {
			InvoiceBooklet b = readOnlyService.module(getModule()).getOne("/linesPerPage");
			return b.getEndId().intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public String getModule() {
		return "invoiceBooklet";
	}

	@Override
	public ReadOnlyService<InvoiceBooklet> getReadOnlyService() {
		return readOnlyService;
	}

	@Override
	public SavingService<InvoiceBooklet> getSavingService() {
		return savingService;
	}

	public List<String> listUsers() {
		return userService.listNamesByRole(SELLER, DRIVER);
	}

	public InvoiceBooklet save(String prefix, String suffix, Long start, Long end, String issuedTo) throws Exception {
		InvoiceBooklet ib = new InvoiceBooklet();
		ib.setPrefix(prefix);
		ib.setSuffix(suffix);
		ib.setStartId(start);
		ib.setEndId(end);
		ib.setIssuedTo(issuedTo);
		return save(ib);
	}

	private String addHyphenIfNotEmpty(String p) {
		return p + (p.isEmpty() ? "" : "-");
	}
}
