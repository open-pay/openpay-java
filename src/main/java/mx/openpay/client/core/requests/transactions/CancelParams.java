package mx.openpay.client.core.requests.transactions;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

public class CancelParams extends RequestBuilder {

	@Getter
	private String chargeId;

	/**
	 * The ID of the charge to refund. Required. The charge must belong to the
	 * merchant, or to the customer if the customer id is set.
	 */
	public CancelParams chargeId(final String chargeId) {
		this.chargeId = chargeId;
		return this;
	}

}
