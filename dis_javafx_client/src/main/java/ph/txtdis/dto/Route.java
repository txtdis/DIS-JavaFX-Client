package ph.txtdis.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.DeliveryType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Route extends EntityCreationTracked<Long> {

	private Account account;

	private DeliveryType type;

	private List<Account> sellerHistory;

	private String name;

	public String getAssignedBy() {
		return getAccount() == null ? null : getAccount().getCreatedBy();
	}

	public ZonedDateTime getAssignedOn() {
		return getAccount() == null ? null : getAccount().getCreatedOn();
	}

	public String getSeller() {
		return getAccount() == null ? null : getAccount().getSeller();
	}

	public String getSeller(LocalDate date) {
		Account account = getAccount(date);
		return account == null ? null : account.getSeller();
	}

	@Override
	public String toString() {
		return name;
	}

	private Account getAccount() {
		return account != null ? account : getAccount(LocalDate.now());
	}

	private Account getAccount(LocalDate date) {
		return getSellerHistory() == null ? null : getLatestAccount(date);
	}

	private Account getLatestAccount(LocalDate date) {
		Optional<Account> a = getSellerHistory().stream().filter(p -> p.getStartDate().compareTo(date) <= 0)
				.max(Account::compareTo);
		return a.isPresent() ? a.get() : null;
	}
}
