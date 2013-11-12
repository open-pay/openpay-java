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
import static mx.openpay.client.utils.OpenpayPathComponents.FEES;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailable;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class Fee extends Transaction {

    private static final String FEES_PATH = MERCHANT_ID + FEES;

    public static Fee create(final String customerId, final BigDecimal amount, final String desc,
            final String orderID) throws ServiceUnavailable, OpenpayServiceException {
        String path = String.format(FEES_PATH, getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", customerId);
        data.put("amount", amount);
        data.put("description", desc);
        data.put("order_id", orderID);
        return getJsonClient().post(path, data, Fee.class);
    }

    public static List<Fee> getList(final SearchParams params) throws ServiceUnavailable,
            OpenpayServiceException {
        String path = String.format(FEES_PATH, getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return getJsonClient().getList(path, map, ListTypes.FEE);
    }

}
