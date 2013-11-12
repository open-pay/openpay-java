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
import static mx.openpay.client.utils.OpenpayPathComponents.WITHDRAWALS;

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
public class Withdrawal extends Transaction {

    private static final String MERCHANT_WITHDRAWALS_PATH = MERCHANT_ID + WITHDRAWALS;

    private static final String GET_MERCHANT_WITHDRAWAL_PATH = MERCHANT_WITHDRAWALS_PATH + ID;

    private static final String CUSTOMER_WITHDRAWALS_PATH = MERCHANT_ID + CUSTOMERS + ID + WITHDRAWALS;

    private static final String GET_CUSTOMER_WITHDRAWAL_PATH = CUSTOMER_WITHDRAWALS_PATH + ID;

    public static Withdrawal createForCustomer(final String customerId, final String destinationId,
            final BigDecimal amount, final String description, final String orderID) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(CUSTOMER_WITHDRAWALS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("destination_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static Withdrawal createForCustomer(final String customerId, final Card card,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_WITHDRAWALS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static Withdrawal createForCustomer(final String customerId, final BankAccount bankAccount,
            final BigDecimal amount, final String description, final String orderID) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(CUSTOMER_WITHDRAWALS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bank_account", bankAccount);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static Withdrawal get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_MERCHANT_WITHDRAWAL_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Withdrawal.class);
    }

    public static Withdrawal get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_WITHDRAWAL_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Withdrawal.class);
    }

    public static Withdrawal createForMerchant(final Card card, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_WITHDRAWALS_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static Withdrawal createForMerchant(final BankAccount bankAccount, final BigDecimal amount,
            final String description, final String orderID) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(MERCHANT_WITHDRAWALS_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("bank_account", bankAccount);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static List<Withdrawal> getList(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(MERCHANT_WITHDRAWALS_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.WITHDRAWAL);
    }

    public static List<Withdrawal> getList(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_WITHDRAWALS_PATH, getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.WITHDRAWAL);
    }
}
