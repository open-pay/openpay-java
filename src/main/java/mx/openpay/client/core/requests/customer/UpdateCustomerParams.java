/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
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
