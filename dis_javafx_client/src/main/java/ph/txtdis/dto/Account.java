package ph.txtdis.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends EntityCreationTracked<Long>implements Comparable<Account> {

	private String seller;

	private LocalDate startDate;

	@Override
	public int compareTo(Account a) {
		return getStartDate().compareTo(a.getStartDate());
	}
}
