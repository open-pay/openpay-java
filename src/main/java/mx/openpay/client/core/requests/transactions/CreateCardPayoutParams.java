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
import mx.openpay.client.core.requests.card.CreateCardParams;
import mx.openpay.client.enums.PayoutMethod;

/**
 * @author elopez
 */
public class CreateCardPayoutParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateCardPayoutParams() {
        this.with("method", PayoutMethod.CARD.name().toLowerCase());
    }

    public CreateCardPayoutParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateCardPayoutParams cardId(final String cardId) {
        return this.with("destination_id", cardId);
    }

    public CreateCardPayoutParams card(final CreateCardParams card) {
        if (card == null) {
            return this.with("card", null);
        } else {
            return this.with("card", card.asMap());
        }
    }

    public CreateCardPayoutParams description(final String description) {
        return this.with("description", description);
    }

    public CreateCardPayoutParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    public CreateCardPayoutParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
