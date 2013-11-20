/*
 * COPYRIGHT © 2012-2013. OPENPAY.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * OPENPAY & OPENCARD IS A REGISTERED TRADEMARK OF OPENCARD INC.
 *
 * This software is confidential and proprietary information of OPENCARD INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.FEES;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Fee;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class FeeOperations extends ServiceOperations {

    private static final String FEES_PATH = MERCHANT_ID + FEES;

    public FeeOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Fee create(final String customerId, final BigDecimal amount, final String desc,
            final String orderID) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FEES_PATH, this.getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", customerId);
        data.put("amount", amount);
        data.put("description", desc);
        data.put("order_id", orderID);
        return this.getJsonClient().post(path, data, Fee.class);
    }

    public List<Fee> list(final SearchParams params) throws ServiceUnavailableException,
            OpenpayServiceException {
        String path = String.format(FEES_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.FEE);
    }

}
