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

import static mx.openpay.client.core.OpenpayAPI.getJsonClient;
import static mx.openpay.client.core.OpenpayAPI.getMerchantId;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.DEPOSITS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;
import static mx.openpay.client.utils.OpenpayPathComponents.SALES;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class Deposit extends Transaction {

    private static final String MERCHANT_DEPOSITS_PATH = MERCHANT_ID + SALES;

    private static final String GET_MERCHANT_DEPOSIT_PATH = MERCHANT_DEPOSITS_PATH + ID;

    private static final String REFUND_MERCHANT_DEPOSIT_PATH = GET_MERCHANT_DEPOSIT_PATH + REFUND;

    private static final String CUSTOMER_DEPOSITS_PATH = MERCHANT_ID + CUSTOMERS + ID + DEPOSITS;

    private static final String GET_CUSTOMER_DEPOSIT_PATH = CUSTOMER_DEPOSITS_PATH + ID;

    private static final String REFUND_CUSTOMER_DEPOSIT_PATH = GET_CUSTOMER_DEPOSIT_PATH + REFUND;

    public static Deposit create(final Card card, final BigDecimal amount, final String description,
            final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_DEPOSITS_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static List<Deposit> list(final SearchParams params) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_DEPOSITS_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.DEPOSIT);
    }

    public static Deposit get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_MERCHANT_DEPOSIT_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Deposit.class);
    }

    public static Deposit refund(final String transactionId, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_MERCHANT_DEPOSIT_PATH, getMerchantId(), transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static Deposit create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(CUSTOMER_DEPOSITS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static Deposit create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(CUSTOMER_DEPOSITS_PATH, getMerchantId(), customerId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Deposit.class);
    }

    public static List<Deposit> getList(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_DEPOSITS_PATH, getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.DEPOSIT);
    }

    public static Deposit get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_DEPOSIT_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Deposit.class);
    }

    public static Deposit refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_CUSTOMER_DEPOSIT_PATH, getMerchantId(), customerId, transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Deposit.class);
    }

    @Getter
    @Setter
    private Refund refund;

}
