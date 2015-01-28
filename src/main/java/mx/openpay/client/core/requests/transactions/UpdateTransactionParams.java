/*
 * COPYRIGHT © 2012-2014. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.transactions;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class UpdateTransactionParams extends RequestBuilder {

    @Getter
    private String chargeId;

    /**
     * The ID of the charge to update. Required.
     */
    public UpdateTransactionParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

    /** New Description. Optional. */
    public UpdateTransactionParams description(final String description) {
        return this.with("description", description);
    }

    /** New Order ID. Optional. */
    public UpdateTransactionParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
