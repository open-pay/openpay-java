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
package mx.openpay.client.core.requests.customer;

import lombok.Getter;
import mx.openpay.client.Address;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class UpdateCustomerParams extends RequestBuilder {

    @Getter
    private String customerId;

    /**
     * ID of the customer to update. Required.
     */
    public UpdateCustomerParams customerId(final String customerId) {
        this.customerId = customerId;
        return this;
    }

    public UpdateCustomerParams name(final String name) {
        return this.with("name", name);
    }

    public UpdateCustomerParams lastName(final String lastName) {
        return this.with("last_name", lastName);
    }

    public UpdateCustomerParams email(final String email) {
        return this.with("email", email);
    }

    public UpdateCustomerParams phoneNumber(final String phoneNumber) {
        return this.with("phone_number", phoneNumber);
    }

    public UpdateCustomerParams address(final Address address) {
        return this.with("address", address);
    }
}
