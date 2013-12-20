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

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateFeeParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateFeeParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateFeeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreateFeeParams description(final String description) {
        return this.with("description", description);
    }

    public CreateFeeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
