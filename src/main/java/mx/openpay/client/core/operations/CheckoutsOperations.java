package mx.openpay.client.core.operations;

import mx.openpay.client.Checkout;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;

import static mx.openpay.client.utils.OpenpayPathComponents.*;
import static mx.openpay.client.utils.OpenpayPathComponents.ID;

public class CheckoutsOperations extends ServiceOperations {
    private static final String FOR_MERCHANT_PATH = MERCHANT_ID + CHECKOUT;
    private static final String FOR_CUSTOMER_PATH = MERCHANT_ID + CUSTOMERS + ID + CHECKOUT;
    private static final String FOR_GET_CHECKOUT_PATH = MERCHANT_ID + CHECKOUT + ID;

    public CheckoutsOperations(JsonServiceClient client) {
        super(client);
    }


    /**
     * Creates checkout at the Customer level.
     *
     * @param request Generic request params.
     * @return Checkout data returned by Openpay
     * @throws OpenpayServiceException     When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */
    public Checkout createCheckout(final String customerId, final RequestBuilder request)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_CUSTOMER_PATH, this.getMerchantId(), customerId);
        return this.getJsonClient().post(path, request.asMap(), Checkout.class);
    }

    /**
     * Create checkou at the Merchant Level
     * @param request
     * @return
     * @throws OpenpayServiceException
     * @throws ServiceUnavailableException
     */
    public Checkout createCheckout(final RequestBuilder request) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_MERCHANT_PATH, this.getMerchantId());
        return this.getJsonClient().post(path, request.asMap(), Checkout.class);
    }

    /**
     * Get a specific checkout by CheckoutId or OrderId
     * @param checkoutOrOrderId
     * @return
     * @throws OpenpayServiceException
     * @throws ServiceUnavailableException
     */
    public Checkout getCheckoutByCheckoutIdOrOrderId(final String checkoutOrOrderId) throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(FOR_GET_CHECKOUT_PATH, this.getMerchantId(), checkoutOrOrderId);
        return this.getJsonClient().get(path, Checkout.class);
    }
}
