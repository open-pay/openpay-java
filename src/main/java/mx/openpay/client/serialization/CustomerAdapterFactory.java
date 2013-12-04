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
