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

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.HttpError;
import mx.openpay.client.exceptions.ServiceUnavailable;

import com.google.gson.reflect.TypeToken;

/**
 * @author elopez
 */
public class Fee extends Transaction {

    private static final Type FEES_LIST_TYPE = new TypeToken<Collection<Fee>>() {
    }.getType();

    private static final String FEES_PATH = "/%s/fees";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Fee create(final String customerId, final BigDecimal amount, final String desc,
            final String orderID) throws ServiceUnavailable, HttpError {
        String path = String.format(FEES_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", customerId);
        data.put("amount", amount);
        data.put("description", desc);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Fee.class);
    }

    public static List<Fee> getList(final Date fromDate, final Date toDate) throws ServiceUnavailable,
            HttpError {
        String path = String.format(FEES_PATH, getMerchantId());
        Map<String, String> params = new HashMap<String, String>();
        params.put("from", formatDate(fromDate));
        params.put("to", formatDate(toDate));
        return getJsonClient().getList(path, params, FEES_LIST_TYPE);
    }

    private static synchronized String formatDate(final Date date) {
        return simpleDateFormat.format(date);
    }
}
