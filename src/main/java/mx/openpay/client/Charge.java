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
import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

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
public class Charge extends Transaction {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + REFUND;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;

    public static Charge create(final Card card, final BigDecimal amount, final String description,
            final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Charge.class);
    }

    public static List<Charge> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public static Charge get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Charge.class);
    }

    public static Charge refund(final String transactionId, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, getMerchantId(), transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Charge.class);
    }

    public static Charge create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Charge.class);
    }

    public static Charge create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Charge.class);
    }

    public static List<Charge> list(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public static Charge get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Charge.class);
    }

    public static Charge refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, getMerchantId(), customerId, transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Charge.class);
    }

    @Getter
    @Setter
    private Refund refund;

}
