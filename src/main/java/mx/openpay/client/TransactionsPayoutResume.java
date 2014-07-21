/**
 * 
 */
package mx.openpay.client;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.gson.annotations.SerializedName;

/**
 * @author Luis Delucio
 *
 */
@Setter
@Getter
@NoArgsConstructor
public class TransactionsPayoutResume {

	@SerializedName("in")
	private BigDecimal in;

	@SerializedName("out")
	private BigDecimal out;

	@SerializedName("charged_adjustments")
	private BigDecimal chargedAdjustments;

	@SerializedName("refunded_adjustments")
	private BigDecimal refundedAdjustments;
}
