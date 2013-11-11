/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client;

import static mx.openpay.client.core.OpenpayApiConfig.getJsonClient;
import static mx.openpay.client.core.OpenpayApiConfig.getMerchantId;
import static mx.openpay.client.utils.OpenpayPaths.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPaths.ID;
import static mx.openpay.client.utils.OpenpayPaths.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPaths.TRANSFERS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class Transfer extends Transaction {

    private static final String MERCHANT_TRANSFERS_PATH = MERCHANT_ID + TRANSFERS;

    private static final String GET_MERCHANT_TRANSFER = MERCHANT_TRANSFERS_PATH + ID;

    private static final String CUSTOMER_TRANSFERS_PATH = MERCHANT_ID + CUSTOMERS + ID + TRANSFERS;

    private static final String GET_CUSTOMER_TRANSFER_PATH = CUSTOMER_TRANSFERS_PATH + ID;

    public static List<Transfer> getList(final SearchParams params) throws HttpError,
            ServiceUnavailable {
        String path = String.format(MERCHANT_TRANSFERS_PATH, getMerchantId());
        return getJsonClient().getList(path, params, ListTypes.TRANSFER);
    }

    public static Transfer get(final String transactionId) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_MERCHANT_TRANSFER, getMerchantId(), transactionId);
        return getJsonClient().get(path, Transfer.class);
    }

    public static Transfer create(final String customerId, final String destinationId, final BigDecimal amount,
            final String description, final String orderID)
            throws ServiceUnavailable, HttpError {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Transfer.class);
    }

    public static List<Transfer> getList(final String customerId, final SearchParams params)
            throws HttpError, ServiceUnavailable {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, getMerchantId(), customerId);
        return getJsonClient().getList(path, params, ListTypes.TRANSFER);
    }

    public static Transfer get(final String customerId, final String transactionId) throws HttpError,
            ServiceUnavailable {
        String path = String.format(GET_CUSTOMER_TRANSFER_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Transfer.class);
    }

}
