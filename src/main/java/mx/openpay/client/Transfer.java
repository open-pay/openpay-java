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

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import com.google.gson.reflect.TypeToken;

/**
 * @author elopez
 */
public class Transfer extends Transaction {

    private static final String MERCHANT_TRANSFERS_PATH = MERCHANT_ID + TRANSFERS;

    private static final String GET_MERCHANT_TRANSFER = MERCHANT_TRANSFERS_PATH + ID;

    private static final String CUSTOMER_TRANSFERS_PATH = MERCHANT_ID + CUSTOMERS + ID + TRANSFERS;

    private static final String GET_CUSTOMER_TRANSFER_PATH = CUSTOMER_TRANSFERS_PATH + ID;

    private static final Type TRANSFER_LIST_TYPE = new TypeToken<Collection<Transfer>>() {
    }.getType();

    public static List<Transfer> getList(final Integer limit, final Integer offset) throws HttpError,
            ServiceUnavailable {
        String path = String.format(MERCHANT_TRANSFERS_PATH, getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, TRANSFER_LIST_TYPE);
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

    public static List<Transfer> getList(final String customerId, final Integer limit, final Integer offset)
            throws HttpError, ServiceUnavailable {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, getMerchantId(), customerId);
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, TRANSFER_LIST_TYPE);
    }

    public static Transfer get(final String customerId, final String transactionId) throws HttpError,
            ServiceUnavailable {
        String path = String.format(GET_CUSTOMER_TRANSFER_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Transfer.class);
    }

}
