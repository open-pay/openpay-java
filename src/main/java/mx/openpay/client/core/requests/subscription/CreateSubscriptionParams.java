/*
 * Copyright 2013 Opencard Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.openpay.client.core.requests.subscription;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.card.CreateCardParams;

/**
 * Request builder to create a new subscription.
 * @author elopez
 */
public class CreateSubscriptionParams extends RequestBuilder {

    @Getter
    private String customerId;

    /**
     * The ID of the customer to subscribe to the plan. Required.
     */
    public CreateSubscriptionParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    /**
     * The ID of the plan to use for the subscription. Required.
     */
    public CreateSubscriptionParams planId(final String planId) {
        return this.with("plan_id", planId);
    }

    /**
     * The ID of the customer's pre-registered card to which the charge will be made each cycle. Required if no card is
     * specified to be created.
     */
    public CreateSubscriptionParams cardId(final String cardId) {
        return this.with("card_id", cardId);
    }

    /**
     * The number of trial days of this subscription. Optional. This option overrides the Plan's trial days.
     */
    public CreateSubscriptionParams trialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }

    /**
     * The card to which the charge will be made each cycle. Required if no pre-registered card id is specified.
     */
    public CreateSubscriptionParams card(final CreateCardParams card) {
        if (card == null) {
            return this.with("card", null);
        } else {
            return this.with("card", card.asMap());
        }
    }

}
