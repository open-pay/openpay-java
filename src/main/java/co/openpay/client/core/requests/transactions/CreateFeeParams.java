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
package co.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import co.openpay.client.core.requests.RequestBuilder;

/**
 * Parameters to charge a fee from a Customer. The amount will be taken from the customer's balance and sent to the
 * merchant's balance.
 * @author elopez
 */
public class CreateFeeParams extends RequestBuilder {

    /**
     * The ID of the customer to charge the Fee to. Required.
     */
    public CreateFeeParams customerId(final String customerId) {
        return this.with("customer_id", customerId);
    }

    /**
     * The amount to charge to the customer, in MXN. Required.
     */
    public CreateFeeParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A description for this fee. Optional.
     */
    public CreateFeeParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * A custom unique identifier for the Fee. Optional.
     */
    public CreateFeeParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
