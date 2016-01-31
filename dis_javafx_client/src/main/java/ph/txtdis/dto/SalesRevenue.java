package ph.txtdis.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalesRevenue implements SellerSold, Keyed<Long> {

	private Long id;

	private String seller, customer;

	private BigDecimal value;
}
