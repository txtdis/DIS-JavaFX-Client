package ph.txtdis.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBookedOrder<PK> extends AbstractSoldOrder<PK> {

	private Booking booking;

	private List<RemittancePayment> payments;

	private BigDecimal unpaidValue;
}