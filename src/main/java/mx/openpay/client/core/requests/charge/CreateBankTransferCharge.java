/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.charge;

import java.math.BigDecimal;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.enums.ChargeMethods;

/**
 * @author elopez
 */
public class CreateBankTransferCharge extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateBankTransferCharge() {
        this.with("method", ChargeMethods.BANK_ACCOUNT.name().toLowerCase());
    }

    public CreateBankTransferCharge withCustomerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateBankTransferCharge withAmount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreateBankTransferCharge withDescription(final String description) {
        return this.with("description", description);
    }

    public CreateBankTransferCharge withOrderId(final String orderId) {
        return this.with("order_id", orderId);
    }
}
