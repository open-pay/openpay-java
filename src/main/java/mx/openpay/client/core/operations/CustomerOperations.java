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
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.util.List;
import java.util.Map;

import mx.openpay.client.Address;
import mx.openpay.client.Customer;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.customer.CreateCustomerParams;
import mx.openpay.client.core.requests.customer.UpdateCustomerParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;

/**
 * Operations for managing Customers.
 * @author elopez
 */
public class CustomerOperations extends ServiceOperations {

    private static final String CUSTOMERS_PATH = MERCHANT_ID + CUSTOMERS;

    private static final String GET_CUSTOMER_PATH = CUSTOMERS_PATH + ID;

    public CustomerOperations(final JsonServiceClient client) {
        super(client);
    }

    public Customer create(final CreateCustomerParams create) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, create.asMap(), Customer.class);
    }

    public List<Customer> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Customer.class);
    }

    public Customer get(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().get(path, Customer.class);
    };

    public Customer update(final UpdateCustomerParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), params.getCustomerId());
        return this.getJsonClient().put(path, params.asMap(), Customer.class);
    }

    public void delete(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        this.getJsonClient().delete(path);
    }

    @Deprecated
    public Customer create(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws OpenpayServiceException,
            ServiceUnavailableException {
        CreateCustomerParams params = new CreateCustomerParams()
                .name(name)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address);
        return this.create(params);
    };

    @Deprecated
    public Customer update(final Customer customer) throws OpenpayServiceException, ServiceUnavailableException {
        return this.update(new UpdateCustomerParams(customer));
    }
}
