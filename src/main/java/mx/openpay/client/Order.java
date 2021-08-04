/**
 * 
 */
package mx.openpay.client;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gson.annotations.SerializedName;

/**
 * @author Luis Delucio
 *
 */
@Deprecated
@Getter
@Setter
@ToString
public class Order {

	private String id;

	@SerializedName("payment_plan_id")
	private String paymentPlanId;

	@SerializedName("customer_id")
	private String customerId;

	@SerializedName("creation_date")
	private Date creationDate;

	@SerializedName("external_order_id")
	private String externalOrderId;

	private String description;

	private BigDecimal amount;

	private String status;

	@SerializedName("total_amount_paid")
	private BigDecimal totalAmountPaid;

	@SerializedName("number_of_payments_made")
	private Integer numberOfPaymentsMade;

	@SerializedName("maximun_number_of_payments")
	private Integer maximunNumberOfPayments;

	@SerializedName("limit_date")
	private Date limitDate;

	@SerializedName("first_pay_limit_date")
	private Date firstPayLimitDate;

	@SerializedName("first_pay_amount")
	private BigDecimal firstPayAmount;

	@SerializedName("total_amount_to_pay")
	private BigDecimal totalAmountToPay;

	private String reference;

	@SerializedName("barcode_url")
	private String barcodeUrl;

	/**
	 * The order's description. Required.
	 */
	public Order description(final String description) {
		this.description = description;
		return this;
	}

	/**
	 * The order's amount. Required.
	 */
	public Order amount(final BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * The ID of the payment plan to use for the order. Required.
	 */
	public Order paymentPlanId(final String paymentPlanId) {
		this.paymentPlanId = paymentPlanId;
		return this;
	}

	/**
	 * The order's external ID. Optional.
	 */
	public Order externalOrderId(final String externalOrderId) {
		this.externalOrderId = externalOrderId;
		return this;
	}

}
