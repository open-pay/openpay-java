package mx.openpay.core.client.full;

import mx.openpay.client.Checkout;
import mx.openpay.client.CheckoutResponse;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchCheckoutParams;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CheckoutTest extends BaseTest{

    @Test
    public void testgetByIdMerchatn() throws ServiceUnavailableException, OpenpayServiceException {
        try {
            String idOpencheckout = "mr6tbtk6xepcsd0ar5yc";

            SearchCheckoutParams searchOpenCheckoutParams = new SearchCheckoutParams();


            searchOpenCheckoutParams.nameOrLastName("JORDY OLIVER 2");
            searchOpenCheckoutParams.limit(1);
            List<CheckoutResponse> response = this.api.checkouts().getCheckoutsByMerchant("mr6tbtk6xepcsd0ar5yc",searchOpenCheckoutParams);
            System.out.println(response.size());
            assertNotNull(response);
        } catch (OpenpayServiceException e) {
            e.printStackTrace();
            assertEquals(404, e.getHttpCode().intValue());
            assertNotNull(e.getErrorCode());
        }
    }
}
