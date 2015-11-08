package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSoldOrder<PK> extends AbstractAuditedId<PK> implements Remarked<PK> {

	private BigDecimal totalValue, grossValue;

	private Long customerId;

	private List<BillableDetail> details;

	private List<String> discounts;

	private List<Long> detailIds, discountIds;

	private LocalDate dueDate, orderDate;

	private Route route;

	private String customerName, customerAddress, remarks;
}
