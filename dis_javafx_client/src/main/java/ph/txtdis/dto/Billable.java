package ph.txtdis.dto;

import static ph.txtdis.util.NumberUtils.isNegative;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Billable extends AbstractAuditedId<Long> implements Remarked<Long> {

	private BigDecimal badOrderAllowanceValue, unpaidValue, totalValue, grossValue;

	private Boolean isRma;

	private List<BillableDetail> details;

	private List<Long> discountIds;

	private List<String> discounts, payments;

	private LocalDate dueDate, orderDate;

	private Long numId, bookingId, customerId, receivingId;

	private String prefix, suffix, customerName, customerAddress, customerLocation, route, remarks, billedBy, printedBy,
			receivedBy, receivingModifiedBy, truck;

	private ZonedDateTime billedOn, printedOn, receivedOn, receivingModifiedOn;

	public String getOrderNo() {
		return prefix() + numId() + suffix();
	}

	@Override
	public String toString() {
		return text() + " No. " + getOrderNo();
	}

	private Long numId() {
		if (numId == null)
			return 0L;
		return isNegative(numId) ? -numId : numId;
	}

	private String prefix() {
		return prefix == null || prefix.isEmpty() ? "" : prefix + "-";
	}

	private String suffix() {
		return suffix == null ? "" : suffix;
	}

	private String text() {
		if (numId == null)
			return "S/O";
		return numId < 0 ? "D/R" : "S/I";
	}
}
