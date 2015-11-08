package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends AbstractAuditedId<Long> implements Remarked<Long> {

	private BigDecimal value;

	private List<PaymentDetail> details;

	private LocalDate paymentDate, depositDate;

	private LocalTime depositTime;

	private Long checkId, depositorBankId, draweeBankId, payorId;

	private String accountNo, draweeBank, depositor, depositorBank, payor, receivedBy, remarks, collector;

	private ZonedDateTime receivedOn, depositorOn;

	@Override
	public String toString() {
		return "Collection ID " + getId();
	}
}
