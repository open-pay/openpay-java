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
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;
import static mx.openpay.client.utils.OpenpayPathComponents.SALES;

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
public class Sales {

    private static final String SALES_PATH = MERCHANT_ID + SALES;

    private static final String GET_SALE_PATH = SALES_PATH + ID;

    private static final String REFUND_SALE_PATH = GET_SALE_PATH + REFUND;

    public static Sales create(final Card card, final BigDecimal amount, final String description, final String orderId)
            throws HttpError, ServiceUnavailable {
        String path = String.format(SALES_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Sales.class);
    }

    public static Sales create(final String sourceId, final BigDecimal amount, final String description,
            final String orderId)
            throws HttpError, ServiceUnavailable {
        String path = String.format(SALES_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        return getJsonClient().post(path, data, Sales.class);
    }

    public static List<Sales> getList(final SearchParams params) throws HttpError, ServiceUnavailable {
        String path = String.format(SALES_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.SALE);
    }

    public static Sales get(final String transactionId) throws HttpError, ServiceUnavailable {
        String path = String.format(GET_SALE_PATH, getMerchantId(), transactionId);
        return getJsonClient().get(path, Sales.class);
    }

    public static void refund(final String transactionId, final String description, final String orderId)
            throws HttpError, ServiceUnavailable {
        String path = String.format(REFUND_SALE_PATH, getMerchantId(), transactionId);
        System.out.println(path);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        getJsonClient().post(path, data, null);
    }
}