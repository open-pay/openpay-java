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

import mx.openpay.client.Address;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateCustomerParams extends RequestBuilder {

    /**
     * Customer first or only name. Required.
     */
    public CreateCustomerParams name(final String name) {
        return this.with("name", name);
    }

    /**
     * Customer last name. Optional.
     */
    public CreateCustomerParams lastName(final String lastName) {
        return this.with("last_name", lastName);
    }

    /**
     * Customer email. Required.
     */
    public CreateCustomerParams email(final String email) {
        return this.with("email", email);
    }

    /**
     * Customer phone number. Optional
     */
    public CreateCustomerParams phoneNumber(final String phoneNumber) {
        return this.with("phone_number", phoneNumber);
    }

    /**
     * Customer address. Optional.
     */
    public CreateCustomerParams address(final Address address) {
        return this.with("address", address);
    }

}
