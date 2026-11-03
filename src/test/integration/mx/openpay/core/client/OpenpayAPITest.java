package mx.openpay.core.client;

import static org.junit.Assert.assertNotNull;
import mx.openpay.client.core.OpenpayAPI;
import org.junit.Test;
public class OpenpayAPITest {

    @Test
    public void testOpenpayAPIConstructorWithParams() {
        // Arrange: Datos de prueba
        String location = "https://sandbox-api.openpay.mx";
        String apiKey = "testApiKey";
        String merchantId = "testMerchantId";

        // Act: Crear instancia de OpenpayAPI
        OpenpayAPI openpayAPI = new OpenpayAPI(location, apiKey, merchantId);

        // Assert: Comprobaciones sobre el estado interno
        assertNotNull("La instancia de OpenpayAPI no debe ser null", openpayAPI);
        
    }
}