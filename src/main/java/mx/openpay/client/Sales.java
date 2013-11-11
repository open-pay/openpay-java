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
import static mx.openpay.client.utils.OpenpayPaths.REFUND;
import static mx.openpay.client.utils.OpenpayPaths.SALES;

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
public class Sales {

    private static final String SALES_PATH = MERCHANT_ID + SALES;

    private static final String GET_SALE_PATH = SALES_PATH + ID;

    private static final String REFUND_SALE_PATH = GET_SALE_PATH + REFUND;

    private static final Type SALES_LIST_TYPE = new TypeToken<Collection<Sales>>() {
    }.getType();

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

    public static List<Sales> getList(final Integer limit, final Integer offset) throws HttpError, ServiceUnavailable {
        String path = String.format(SALES_PATH, getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        params.put("limit", String.valueOf(limit));
        params.put("offset", String.valueOf(offset));
        return getJsonClient().getList(path, params, SALES_LIST_TYPE);
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
