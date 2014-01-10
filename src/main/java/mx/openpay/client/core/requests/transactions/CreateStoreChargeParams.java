/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;

/**
 * @author Luis Delucio
 *
 */
public class CreateStoreChargeParams extends RequestBuilder {

	/**
	 * 
	 */
	public CreateStoreChargeParams() {
		this.with("method", ChargeMethods.STORE.name().toLowerCase());
	}

	/**
	 * The amount to charge the customer. Required. The customer should pay this
	 * exact amount.
	 */
	public CreateStoreChargeParams amount(final BigDecimal amount) {
		return this.with("amount", amount);
	}

	/**
	 * A description for the charge. Optional.
	 */
	public CreateStoreChargeParams description(final String description) {
		return this.with("description", description);
	}

	/**
	 * A custom unique ID to identify the charge. Optional.
	 */
	public CreateStoreChargeParams orderId(final String orderId) {
		return this.with("order_id", orderId);
	}

}
