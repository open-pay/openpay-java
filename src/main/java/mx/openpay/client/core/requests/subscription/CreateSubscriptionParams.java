/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.requests.subscription;

import lombok.Getter;
import mx.openpay.client.Card;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * Request builder to create a new subscription.
 * @author elopez
 */
public class CreateSubscriptionParams extends RequestBuilder {

    @Getter
    private String customerId;

    public CreateSubscriptionParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public CreateSubscriptionParams planId(final String planId) {
        return this.with("plan_id", planId);
    }

    public CreateSubscriptionParams cardId(final String cardId) {
        return this.with("card_id", cardId);
    }

    public CreateSubscriptionParams trialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }

    public CreateSubscriptionParams card(final Card card) {
        return this.with("card", card);
    }

}
