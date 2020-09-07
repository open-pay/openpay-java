package mx.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionFee {
	
	private BigDecimal amount;
	
	private BigDecimal tax;
}
