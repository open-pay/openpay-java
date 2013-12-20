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

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class RefundParams extends RequestBuilder {

    @Getter
    private String customerId;

    @Getter
    private String chargeId;

    public RefundParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public RefundParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

}
