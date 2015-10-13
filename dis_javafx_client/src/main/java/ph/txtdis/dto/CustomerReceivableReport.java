package ph.txtdis.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CustomerReceivableReport {

	private List<CustomerReceivable> receivables;

	private List<BigDecimal> totals;

	private String customer;

	private ZonedDateTime timestamp;
}
