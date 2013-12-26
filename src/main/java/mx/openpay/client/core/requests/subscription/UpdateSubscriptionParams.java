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

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import mx.openpay.client.Subscription;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.core.requests.card.CreateCardParams;

/**
 * @author elopez
 */
public class UpdateSubscriptionParams extends RequestBuilder {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Getter
    private String customerId;

    @Getter
    private String subscriptionId;

    /**
     * Initializes the Subscription update parameters with the values of the current subscription. The only exception is
     * the Card information, which needs to be set into the params if the card must be updated.
     * @param subscription
     */
    public UpdateSubscriptionParams(final Subscription subscription) {
        this.customerId = subscription.getCustomerId();
        this.subscriptionId = subscription.getId();
        this.trialEndDate(subscription.getTrialEndDate());
        this.cancelAtPeriodEnd(subscription.getCancelAtPeriodEnd());
    }

    /**
     * A new date to end the user's trial period. This overrides the starting trial days set.
     */
    public UpdateSubscriptionParams trialEndDate(final Date trialEndDate) {
        if (trialEndDate == null) {
            return this.with("trial_end_date", null);
        } else {
            return this.with("trial_end_date", this.dateFormat.format(trialEndDate));
        }
    }

    /**
     * Whether to cancel the subscription when the current cycle ends. Can be set to false to undo the change before the
     * subscription is cancelled.
     */
    public UpdateSubscriptionParams cancelAtPeriodEnd(final Boolean cancelAtPeriodEnd) {
        return this.with("cancel_at_period_end", cancelAtPeriodEnd);
    }

    /**
     * Change the card to which this subscription is charged to a completely new card. If the subscription is in unpaid
     * status, it will change to past_due and charges will be attempted again with the new information.
     */
    public UpdateSubscriptionParams card(final CreateCardParams card) {
        if (card == null) {
            return this.with("card", null);
        } else {
            return this.with("card", card.asMap());
        }
    }

    /**
     * Change the card to which this subscription is charged to a preregistered card. If the subscription is in unpaid
     * status, it will change to past_due and charges will be attempted again with the new information.
     */
    public UpdateSubscriptionParams cardId(final String cardId) {
        return this.with("card_id", cardId);
    }

}
