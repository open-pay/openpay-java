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

/**
 * @author elopez
 */
public class UpdatePlanParams extends RequestBuilder {

    @Getter
    private String planId;

    public UpdatePlanParams planId(final String planId) {
        this.planId = planId;
        return this;
    }

    public UpdatePlanParams name(final String name) {
        return this.with("name", name);
    }

    public UpdatePlanParams trialDays(final Integer trialDays) {
        return this.with("trial_days", trialDays);
    }
}
