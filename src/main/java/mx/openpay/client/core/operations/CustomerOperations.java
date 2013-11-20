/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Address;
import mx.openpay.client.Customer;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class CustomerOperations extends ServiceOperations {

    private static final String CUSTOMERS_PATH = MERCHANT_ID + CUSTOMERS;

    private static final String GET_CUSTOMER_PATH = CUSTOMERS_PATH + ID;

    public CustomerOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Customer create(final String name, final String lastName, final String email,
            final String phoneNumber, final Address address) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("last_name", lastName);
        data.put("email", email);
        data.put("phone_number", phoneNumber);
        data.put("address", address);
        return this.getJsonClient().post(path, data, Customer.class);
    };

    public List<Customer> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMERS_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.CUSTOMER);
    }

    public Customer get(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().get(path, Customer.class);
    };

    public Customer update(final Customer customer) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customer.getId());
        return this.getJsonClient().put(path, customer, Customer.class);
    }

    public void delete(final String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_PATH, this.getMerchantId(), customerId);
        this.getJsonClient().delete(path);
    }

}
