/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.serialization;

import java.text.SimpleDateFormat;

import mx.openpay.client.Subscription;

import com.google.gson.JsonElement;

/**
 * @author elopez
 */
public class SubscriptionAdapterFactory extends OpenpayTypeAdapterFactory<Subscription> {

    /**
     * @param clazz
     */
    public SubscriptionAdapterFactory() {
        super(Subscription.class);
    }

    /**
     * @see mx.openpay.client.serialization.OpenpayTypeAdapterFactory#beforeWrite(java.lang.Object,
     *      com.google.gson.JsonElement)
     */
    @Override
    protected void beforeWrite(final Subscription value, final JsonElement tree) {
        if (tree.isJsonObject() && tree.getAsJsonObject().has("trial_end_date") && value.getTrialEndDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            tree.getAsJsonObject().addProperty("trial_end_date", dateFormat.format(value.getTrialEndDate()));
        }
    }

}
