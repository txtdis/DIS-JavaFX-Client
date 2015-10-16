package ph.txtdis.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ph.txtdis.type.ReceiptReferenceType;

@Data
@EqualsAndHashCode(callSuper = true)
public class Receiving extends AbstractTrackedId<Long> {

	private Customer customer;

	private LocalDate orderDate;

	private String remarks;

	private String partnerReferenceId;

	private ReceiptReferenceType reference;

	private Long referenceId;

	private User checker;

	private List<OrderDetail> details;
}
