package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RemittanceDetail extends AbstractAuditedId<Long> {

	private Remittance remittance;

	private Invoice invoice;

	private BigDecimal paymentValue;
}
