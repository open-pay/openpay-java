package mx.openpay.client.core.operations;

import static mx.openpay.client.utils.OpenpayPathComponents.CHARGES;
import static mx.openpay.client.utils.OpenpayPathComponents.CUSTOMERS;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;
import static mx.openpay.client.utils.OpenpayPathComponents.MERCHANT_ID;
import static mx.openpay.client.utils.OpenpayPathComponents.REFUND;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.enums.ChargeMethods;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.ListTypes;
import mx.openpay.client.utils.SearchParams;

/**
 * @author elopez
 */
public class ChargeOperations extends ServiceOperations {

    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHARGES;

    private static final String GET_FOR_MERCHANT_PATH = FOR_MERCHANT_PATH + ID;

    private static final String REFUND_FOR_MERCHANT_PATH = GET_FOR_MERCHANT_PATH + REFUND;

    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHARGES;

    private static final String GET_FOR_CUSTOMER_PATH = FOR_CUSTOMER_PATH + ID;

    private static final String REFUND_FOR_CUSTOMER_PATH = GET_FOR_CUSTOMER_PATH + REFUND;

    public ChargeOperations(final JsonServiceClient client, final String merchantId) {
        super(client, merchantId);
    }

    public Charge create(final Card card, final BigDecimal amount, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        data.put("method", ChargeMethods.CARD.name().toLowerCase());
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public Charge createMerchantBankTransfer(final BigDecimal amount, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        data.put("method", ChargeMethods.BANK_ACCOUNT.name().toLowerCase());
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public List<Charge> list(final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public Charge get(final String transactionId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(GET_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    public Charge refund(final String transactionId, final String description, final String orderId)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_MERCHANT_PATH, this.getMerchantId(), transactionId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public Charge create(final String customerId, final Card card, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("card", card);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        data.put("method", ChargeMethods.CARD.name().toLowerCase());
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public Charge create(final String customerId, final String sourceId, final BigDecimal amount,
            final String description, final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("source_id", sourceId);
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        data.put("method", ChargeMethods.CARD.name().toLowerCase());
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public Charge createCustomerBankTransfer(final String customerId, final BigDecimal amount,
            final String description,
            final String orderId) throws ServiceUnavailableException, OpenpayServiceException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("amount", amount);
        data.put("description", description);
        data.put("order_id", orderId);
        data.put("method", ChargeMethods.BANK_ACCOUNT.name().toLowerCase());
        return this.getJsonClient().post(path, data, Charge.class);
    }

    public List<Charge> list(final String customerId, final SearchParams params) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().list(path, map, ListTypes.CHARGE);
    }

    public Charge get(final String customerId, final String transactionId) throws OpenpayServiceException,
            ServiceUnavailableException {
        String path = String.format(GET_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        return this.getJsonClient().get(path, Charge.class);
    }

    public Charge refund(final String customerId, final String transactionId, final String description,
            final String orderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(REFUND_FOR_CUSTOMER_PATH, this.getMerchantId(), customerId, transactionId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("description", description);
        data.put("order_id", orderId);
        return this.getJsonClient().post(path, data, Charge.class);
    }

}
