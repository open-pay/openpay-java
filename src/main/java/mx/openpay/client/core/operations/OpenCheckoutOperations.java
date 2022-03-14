package mx.openpay.client.core.operations;

import mx.openpay.client.OpenCheckoutConfigurationResponse;
import mx.openpay.client.OpenCheckoutConfigurationSearchResponse;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.requests.RequestBuilder;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchOpenCheckoutParams;

import java.util.Date;
import java.util.Map;

public class OpenCheckoutOperations extends ServiceOperations{

    private static final String OPEN_CHECKOUT_CONFIGURATIONS_PAGE = "/open-checkout-configurations";

    private static final String OPEN_CHECKOUT_CONFIGURATION = "/%s/open-checkout-configuration";

    private static final String OPEN_CHECKOUT_CONFIGURATION_DETAIL = "/%s" + OPEN_CHECKOUT_CONFIGURATIONS_PAGE + "/%s/";


    private static final String OPEN_CHECKOUT_CONFIGURATION_MERCHANT = "/%s" + OPEN_CHECKOUT_CONFIGURATIONS_PAGE;


    public OpenCheckoutOperations(JsonServiceClient client) {
        super(client);
    }


    /**
     * Creates open checkout at the Customer level.
     *
     * @param request Generic request params.
     * @return OpenCheckout data returned by Openpay
     * @throws OpenpayServiceException     When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */
    public OpenCheckoutConfigurationResponse createOpenCheckout( final RequestBuilder request)
            throws OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(OPEN_CHECKOUT_CONFIGURATION, this.getMerchantId());
        return this.getJsonClient().post(path,  request.asMap(), OpenCheckoutConfigurationResponse.class);
    }



    /**
     * Permite consultar las configuraciones del comercio (que no hayan sido eliminadas).
     *
     * @param merchantId El identificador público del comercio.
     * @param params parametros para la busqueda
     * @return La lista de configuraciones.
     * @throws OpenpayServiceException     When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */

    public OpenCheckoutConfigurationSearchResponse getConfigurationsByMerchant(final String merchantId, SearchOpenCheckoutParams params) throws  OpenpayServiceException, ServiceUnavailableException {

        String path = String.format(OPEN_CHECKOUT_CONFIGURATION_MERCHANT, this.getMerchantId());

        Map<String, String> map = params == null ? null : params.asMap();
        return this.getJsonClient().get(path,map,OpenCheckoutConfigurationSearchResponse.class);
    }

    /**
     * Permite consultar una configuracion para un comercio por id
     *
     * @param merchantId El identificador público del comercio.
     * @param idOpenCheckout identificador del open checkput
     * @return La lista de configuraciones.
     * @throws OpenpayServiceException     When Openpay returns an error response
     * @throws ServiceUnavailableException When an unexpected communication error occurs.
     */

    public OpenCheckoutConfigurationResponse getConfigurationsById(final String merchantId, String idOpenCheckout) throws  OpenpayServiceException, ServiceUnavailableException {
        String path = String.format(OPEN_CHECKOUT_CONFIGURATION_DETAIL, this.getMerchantId(),idOpenCheckout);

        return this.getJsonClient().get(path,OpenCheckoutConfigurationResponse.class);
    }







}
