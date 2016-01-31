package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreditNote extends EntityModificationTracked<Long> {

	private BigDecimal balanceValue, totalValue;

	private LocalDate creditDate;

	private String description, remarks;

	private List<CreditNotePayment> payments;
}
