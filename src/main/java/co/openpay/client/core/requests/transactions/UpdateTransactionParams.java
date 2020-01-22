/*
 * Copyright 2012 - 2015 Opencard Inc.
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
package co.openpay.client.core.requests.transactions;

import co.openpay.client.core.requests.RequestBuilder;
import lombok.Getter;

/**
 * @author Eli Lopez, eli.lopez@opencard.mx
 */
public class UpdateTransactionParams extends RequestBuilder {

    @Getter
    private String chargeId;

    /**
     * The ID of the charge to update. Required.
     */
    public UpdateTransactionParams chargeId(final String chargeId) {
        this.chargeId = chargeId;
        return this;
    }

    /** New Description. Optional. */
    public UpdateTransactionParams description(final String description) {
        return this.with("description", description);
    }

    /** New Order ID. Optional. */
    public UpdateTransactionParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
