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
package co.openpay.client.core.operations.groups;

import static co.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static co.openpay.client.utils.OpenpayPathComponents.GROUPS;
import static co.openpay.client.utils.OpenpayPathComponents.ID;

import java.util.List;
import java.util.Map;

import co.openpay.client.Customer;
import co.openpay.client.core.JsonServiceClient;
import co.openpay.client.core.operations.ServiceOperations;
import co.openpay.client.exceptions.OpenpayServiceException;
import co.openpay.client.exceptions.ServiceUnavailableException;
import co.openpay.client.utils.SearchParams;

/**
 * Operations for managing customer within a group.
 * @author elopez
 */
public class GroupCustomerOperations extends ServiceOperations {

    private static final String GROUP_CUSTOMERS_PATH = GROUPS + ID + CUSTOMERS;

    private static final String GET_GROUP_CUSTOMER_PATH = GROUP_CUSTOMERS_PATH + ID;

    public GroupCustomerOperations(final JsonServiceClient client) {
        super(client);
    }

    public Customer create(final Customer create) throws OpenpayServiceException,  ServiceUnavailableException {
        String path = String.format(GROUP_CUSTOMERS_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, create, Customer.class);
    }

    public List<Customer> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GROUP_CUSTOMERS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, Customer.class);
    }

    public Customer get(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_GROUP_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().get(path, Customer.class);
    };

    public Customer update(final Customer customer) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_GROUP_CUSTOMER_PATH, this.getMerchantId(), customer.getId());
        return this.getJsonClient().put(path, customer, Customer.class);
    }

    public void delete(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_GROUP_CUSTOMER_PATH, this.getMerchantId(), customerId);
        this.getJsonClient().delete(path);
    }

}
