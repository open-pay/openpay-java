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

import mx.openpay.client.Customer;

import com.google.gson.JsonElement;

/**
 * Modifies the generated Customer JSON to remove deserialization-only fields.
 * @author elopez
 */
public class CustomerAdapterFactory extends OpenpayTypeAdapterFactory<Customer> {

    public CustomerAdapterFactory() {
        super(Customer.class);
    }

    @Override
    protected void beforeWrite(final Customer value, final JsonElement tree) {
        if (tree.isJsonObject()) {
            tree.getAsJsonObject().remove("status");
            tree.getAsJsonObject().remove("creation_date");
            tree.getAsJsonObject().remove("balance");
        }
    }

}
