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
public class UpdateSubscription extends RequestBuilder {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Getter
    private final String customerId;

    @Getter
    private final String subscriptionId;

    public UpdateSubscription(final String customerId, final String subscriptionId) {
        this.customerId = customerId;
        this.subscriptionId = subscriptionId;
    }

    public UpdateSubscription withTrialEndDate(final Date trialEndDate) {
        if (trialEndDate == null) {
            return this.with("trial_end_date", null);
        } else {
            return this.with("trial_end_date", this.dateFormat.format(trialEndDate));
        }
    }

    public UpdateSubscription withCancelAtPeriodEnd(final Boolean cancelAtPeriodEnd) {
        return this.with("cancel_at_period_end", cancelAtPeriodEnd);
    }

    public UpdateSubscription withCard(final Card card) {
        return this.with("card", card);
    }

    public UpdateSubscription withCardId(final String cardId) {
        return this.with("card_id", cardId);
    }

}
