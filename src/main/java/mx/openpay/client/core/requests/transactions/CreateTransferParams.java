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
package mx.openpay.client.core.requests.transactions;

import java.math.BigDecimal;

import lombok.Getter;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * Parameters to transfer money between two customers.
 * @author elopez
 */
public class CreateTransferParams extends RequestBuilder {

    @Getter
    private String fromCustomerId;

    /**
     * The Customer from which the amount will be taken. Required.
     */
    public CreateTransferParams fromCustomerId(final String fromCustomerId) {
        this.fromCustomerId = fromCustomerId;
        return this;
    }

    /**
     * The Customer to which the amount will be deposited. Required.
     */
    public CreateTransferParams toCustomerId(final String toCustomerId) {
        return this.with("customer_id", toCustomerId);
    }

    /**
     * The amount to transfer, in MXN. Required.
     */
    public CreateTransferParams amount(final BigDecimal amount) {
        return this.with("amount", amount);
    }

    /**
     * A description for this transfer. Optional.
     */
    public CreateTransferParams description(final String description) {
        return this.with("description", description);
    }

    /**
     * A custom unique identifier for the transfer. Optional. This will be set only in the transaction belonging to the
     * customer from which the amount was taken.
     */
    public CreateTransferParams orderId(final String orderId) {
        return this.with("order_id", orderId);
    }

}
