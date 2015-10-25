package ph.txtdis.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSoldOrder<PK> extends AbstractAuditedId<PK> {

	private Customer customer;

	private LocalDate orderDate;

	private String remarks;

	private List<SoldOrderDetail> details;

	private CreditDetail credit;

	private List<Discount> discounts;

	private Route route;
}
