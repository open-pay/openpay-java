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

import mx.openpay.client.Address;
import mx.openpay.client.core.requests.RequestBuilder;

/**
 * @author elopez
 */
public class CreateCustomer extends RequestBuilder {

    public CreateCustomer withName(final String name) {
        return this.with("name", name);
    }

    public CreateCustomer withLastName(final String lastName) {
        return this.with("last_name", lastName);
    }

    public CreateCustomer withEmail(final String email) {
        return this.with("email", email);
    }

    public CreateCustomer withPhoneNumber(final String phoneNumber) {
        return this.with("phone_number", phoneNumber);
    }

    public CreateCustomer withAddress(final Address address) {
        return this.with("address", address);
    }

}
