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
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.DEPOSITS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

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
public class Deposit extends Transaction {

    private static final String DEPOSITS_PATH = MERCHANT_ID + CUSTOMERS + ID + DEPOSITS;

    private static final String GET_DEPOSIT_PATH = DEPOSITS_PATH + ID;

    private static final String REFUND_DEPOSIT_PATH = GET_DEPOSIT_PATH + REFUND;

    public static Deposit create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderID) throws ServiceUnavailable, HttpError {
        String path = String.format(DEPOSITS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static Deposit create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderID) throws ServiceUnavailable, HttpError {
        String path = String.format(DEPOSITS_PATH, getMerchantId(), customerId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static List<Deposit> getList(final String customerId, final SearchParams params)
            throws HttpError, ServiceUnavailable {
        String path = String.format(DEPOSITS_PATH, getMerchantId(), customerId);
        return getJsonClient().getList(path, params, ListTypes.DEPOSIT);
    }

    public static Deposit get(final String customerId, final String transactionId) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_DEPOSIT_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Deposit.class);
    }

    public static void refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws HttpError, ServiceUnavailable {
        String path = String.format(REFUND_DEPOSIT_PATH, getMerchantId(), customerId, transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        getJsonClient().post(path, data, null);
    }

}
