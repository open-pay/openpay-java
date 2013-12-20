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

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import mx.openpay.client.Card;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class UpdateSubscriptionParams extends RequestBuilder {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Getter
    private String customerId;

    @Getter
    private String subscriptionId;

    public UpdateSubscriptionParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public UpdateSubscriptionParams subscriptionId(final String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public UpdateSubscriptionParams trialEndDate(final Date trialEndDate) {
        if (trialEndDate == null) {
            return this.with("trial_end_date", null);
        } else {
            return this.with("trial_end_date", this.dateFormat.format(trialEndDate));
        }
    }

    public UpdateSubscriptionParams cancelAtPeriodEnd(final Boolean cancelAtPeriodEnd) {
        return this.with("cancel_at_period_end", cancelAtPeriodEnd);
    }

    public UpdateSubscriptionParams card(final Card card) {
        return this.with("card", card);
    }

    public UpdateSubscriptionParams cardId(final String cardId) {
        return this.with("card_id", cardId);
    }

}
