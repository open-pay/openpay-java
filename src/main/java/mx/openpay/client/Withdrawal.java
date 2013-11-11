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
import static mx.openpay.client.utils.OpenpayPaths.ID;
import static mx.openpay.client.utils.OpenpayPaths.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPaths.WITHDRAWALS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

/**
 * @author elopez
 */
public class Withdrawal extends Transaction {

    private static final String MERCHANT_WITHDRAWALS_PATH = MERCHANT_ID + WITHDRAWALS;

    private static final String GET_MERCHANT_WITHDRAWAL_PATH = MERCHANT_WITHDRAWALS_PATH + ID;

    private static final String CUSTOMER_WITHDRAWAL_PATH = "/%s/customers/%s/withdrawals";

    private static final String GET_CUSTOMER_WITHDRAWAL_PATH = CUSTOMER_WITHDRAWAL_PATH + ID;

    public static Withdrawal createForCustomer(final String customerId, final String destinationId,
            final BigDecimal amount, final String description, final String orderID) throws ServiceUnavailable,
            HttpError {
        String path = String.format(CUSTOMER_WITHDRAWAL_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("destination_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Withdrawal.class);
    }

    public static Withdrawal createForCustomer(final String customerId, final Card card,
            final BigDecimal amount, final String description, final String orderID) {
        return null;
    }

    public static Withdrawal createForCustomer(final String customerId, final BankAccount bankAccount,
            final BigDecimal amount, final String description, final String orderID) {
        return null;
    }

    public static Withdrawal get(final String transactionId) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_MERCHANT_WITHDRAWAL_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Withdrawal.class);
    }

    public static Withdrawal get(final String customerId, final String transactionId) throws HttpError,
            ServiceUnavailable {
        String path = String.format(GET_CUSTOMER_WITHDRAWAL_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Withdrawal.class);
    }

    public static Withdrawal createForMerchant(final String destinationId, final BigDecimal amount,
            final String description, final String orderID) {
        return null;
    }

    public static Withdrawal createForMerchant(final Card card, final BigDecimal amount,
            final String description, final String orderID) {
        return null;
    }

    public static Withdrawal createForMerchant(final BankAccount bankAccount, final BigDecimal amount,
            final String description, final String orderID) {
        return null;
    }

    public static List<Withdrawal> getList() {
        return null;
    }

    public static List<Withdrawal> getList(final String customerId) {
        return null;
    }
}
