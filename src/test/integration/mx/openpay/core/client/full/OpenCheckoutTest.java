package mx.openpay.core.client.full;


import mx.openpay.client.OpenCheckoutConfigurationResponse;
import mx.openpay.client.OpenCheckoutConfigurationResponseLight;
import mx.openpay.client.core.requests.transactions.CreateOpenCheckoutParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchOpenCheckoutParams;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class OpenCheckoutTest extends BaseTest{

    @Test
    public void testgetById() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            String idOpencheckout = "occen6olmiybj77ixuq6";
            OpenCheckoutConfigurationResponse response = this.api.openCheckout().getConfigurationsById("movcg01xcf9kggrron0e",idOpencheckout);
            assertNotNull(response);
        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }


    @Test
    public void testAddOpenCheckout() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            CreateOpenCheckoutParams openCheckout = new CreateOpenCheckoutParams()
                    .with("name", "prueba")
                    .with("amount", 200)
                    .with("description", "aaaa")
                    .with("redirect_url", "prueba.com")
                    .with("currency","MXN")
                    .with("iva", 2)
                    .with("expiration_date",  null)
                    .with("open_amount", true);
            OpenCheckoutConfigurationResponse response = this.api.openCheckout().createOpenCheckout(openCheckout);
            System.out.println(response);
        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }


    @Test
    public void testUpdatepenCheckout() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            CreateOpenCheckoutParams openCheckout = new CreateOpenCheckoutParams()
                    .with("name", "prueba")
                    .with("id", "occen6olmiybj77ixuq6")
                    .with("amount", 200)
                    .with("description", "aaaa")
                    .with("redirect_url", "prueba.com")
                    .with("currency","MXN")
                    .with("iva", 2)
                    .with("expiration_date",  null)
                    .with("open_amount", true);
            OpenCheckoutConfigurationResponse response = this.api.openCheckout().updateOpenCheckout(openCheckout);
            System.out.println(response);
        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }


    @Test
    public void testDeleteById() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            String idOpencheckout = "occen6olmiybj77ixuq6";
            this.api.openCheckout().deleteOpencheckout("movcg01xcf9kggrron0e",idOpencheckout);

        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }


    @Test
    public void testgetBylight() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            List<OpenCheckoutConfigurationResponseLight> response = this.api.openCheckout().getConfigurationsLight("myjsc2gdtfccbzgxdqza");
            assertNotNull(response);
        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            //assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }

}
