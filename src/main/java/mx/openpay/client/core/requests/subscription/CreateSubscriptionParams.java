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
public class CreateSubscription extends RequestBuilder {

    @Getter
    private final String customerId;

    public CreateSubscription(final String customerId) {
        this.customerId = customerId;
    }

    public CreateSubscription withPlanId(final String planId) {
        return this.with("plan_id", planId);
    }

    public CreateSubscription withCardId(final String cardId) {
        return this.with("card_id", cardId);
    }

    public CreateSubscription withTrialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }

    public CreateSubscription withCard(final Card card) {
        return this.with("card", card);
    }

}
