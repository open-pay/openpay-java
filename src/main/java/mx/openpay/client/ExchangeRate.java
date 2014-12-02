/**
 * 
 */
package mx.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * @author Luis Delucio
 *
 */
@Getter
@ToString
public class ExchangeRate {

	@SerializedName("from")
	private String fromCurrency;

	@SerializedName("to")
	private String toCurrency;

	private Date date;

	private BigDecimal value;
}
