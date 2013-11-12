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
import static mx.openpay.client.utils.OpenpayPathComponents.TRANSFERS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.enums.TransactionType;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
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

    public static List<Transfer> getList(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(MERCHANT_TRANSFERS_PATH, getMerchantId());
        return getJsonClient().getList(path, params == null ? null : params.asMap(), ListTypes.TRANSFER);
    }

    public static Transfer get(final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        return get(transactionId, TransactionType.TRANSFER_FROM);
    }

    public static Transfer get(final String transactionId, final TransactionType type) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_MERCHANT_TRANSFER, getMerchantId(), transactionId);
        Map<String, String> map = new HashMap<String, String>();
        if (type == null) {
            map.put("transaction_type", TransactionType.TRANSFER_FROM.name().toLowerCase());
        } else {
            map.put("transaction_type", type.name().toLowerCase());
        }
        return getJsonClient().get(path, map, Transfer.class);
    }

    public static Transfer create(final String customerId, final String destinationId, final BigDecimal amount,
            final String description, final String orderID)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Transfer.class);
    }

    public static List<Transfer> getList(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, getMerchantId(), customerId);
        return getJsonClient().getList(path, params == null ? null : params.asMap(), ListTypes.TRANSFER);
    }

    public static Transfer get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_TRANSFER_PATH, getMerchantId(), customerId, transactionId);
        return getJsonClient().get(path, Transfer.class);
    }

}
