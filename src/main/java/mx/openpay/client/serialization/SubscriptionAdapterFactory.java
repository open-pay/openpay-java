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
