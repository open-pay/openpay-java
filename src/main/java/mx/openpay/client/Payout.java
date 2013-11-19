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
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.PAYOUTS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class Payout extends Transaction {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + PAYOUTS;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + PAYOUTS;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    public static Payout createForCustomer(final String customerId, final String destinationId,
            final BigDecimal amount, final String description, final String orderID)
            throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("destination_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Payout.class);
    }

    public static Payout createForCustomer(final String customerId, final Card card,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Payout.class);
    }

    public static Payout createForCustomer(final String customerId, final BankAccount bankAccount,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bank_account", bankAccount);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Payout.class);
    }

    public static Payout get(final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Payout.class);
    }

    public static Payout get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Payout.class);
    }

    public static Payout createForMerchant(final Card card, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Payout.class);
    }

    public static Payout createForMerchant(final BankAccount bankAccount, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bank_account", bankAccount);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Payout.class);
    }

    public static List<Payout> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().list(path, map, ListTypes.PAYOUT);
    }

    public static List<Payout> list(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().list(path, map, ListTypes.PAYOUT);
    }
}
