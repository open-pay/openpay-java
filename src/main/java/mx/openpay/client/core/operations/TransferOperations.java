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

import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.TRANSFERS;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Transfer;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.enums.OperationType;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class TransferOperations extends ServiceOperations {

    private static final String MERCHANT_TRANSFERS_PATH = MERCHANT_ID + TRANSFERS;

    private static final String GET_MERCHANT_TRANSFER = MERCHANT_TRANSFERS_PATH + ID;

    private static final String CUSTOMER_TRANSFERS_PATH = MERCHANT_ID + CUSTOMERS + ID + TRANSFERS;

    private static final String GET_CUSTOMER_TRANSFER_PATH = CUSTOMER_TRANSFERS_PATH + ID;

    /**
     * @param client
     */
    public TransferOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
        // TODO Auto-generated constructor stub
    }

    public List<Transfer> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(MERCHANT_TRANSFERS_PATH, this.getMerchantId());
        return this.getJsonClient().list(path, params == null ? null : params.asMap(), ListTypes.TRANSFER);
    }

    public Transfer get(final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        return this.get(transactionId, OperationType.OUT);
    }

    public Transfer get(final String transactionId, final OperationType type) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_MERCHANT_TRANSFER, this.getMerchantId(), transactionId);
        Map<String, String> map = new HashMap<String, String>();
        if (type == null) {
            map.put("operation_type", OperationType.OUT.name().toLowerCase());
        } else {
            map.put("operation_type", type.name().toLowerCase());
        }
        return this.getJsonClient().get(path, map, Transfer.class);
    }

    public Transfer create(final String customerId, final String destinationId, final BigDecimal amount,
            final String description, final String orderID)
            throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customer_id", destinationId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderID);
        return this.getJsonClient().post(path, data, Transfer.class);
    }

    public List<Transfer> list(final String customerId, final SearchParams params)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(CUSTOMER_TRANSFERS_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().list(path, params == null ? null : params.asMap(), ListTypes.TRANSFER);
    }

    public Transfer get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_CUSTOMER_TRANSFER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Transfer.class);
    }

}
