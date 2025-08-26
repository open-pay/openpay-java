package mx.openpay.core.client.full;

import mx.openpay.client.CheckoutResponse;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.CheckoutsOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchCheckoutParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckoutUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private CheckoutsOperations checkoutsOperations;

    /**
     * Conversión de: testgetByIdMerchatn (renombrado para mayor claridad)
     * Lógica: Simulamos la respuesta de la API al buscar checkouts y verificamos
     * que el método devuelve la lista de resultados correctamente.
     */
    @Test
    void testGetCheckoutsByMerchant() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Preparamos los datos de entrada para el método.
        String merchantIdToSearch = "mr6tbtk6xepcsd0ar5yc";
        SearchCheckoutParams searchParams = new SearchCheckoutParams();
        searchParams.limit(1);

        // 2. Creamos la lista de respuesta que simulará el resultado de la API.
        CheckoutResponse checkout1 = new CheckoutResponse();
        checkout1.setId("cko_test_123");
        CheckoutResponse checkout2 = new CheckoutResponse();
        checkout2.setId("cko_test_456");
        List<CheckoutResponse> mockedResponseList = Arrays.asList(checkout1, checkout2);

        // 3. Configuramos el mock para las llamadas que ocurren dentro del método.
        // Primero, la llamada a getMerchantId() que se usa para construir la URL.
        when(this.jsonServiceClientMock.getMerchantId()).thenReturn("fake_merchant_id");
        // Segundo, la llamada a list() que es la que trae los datos.
        when(this.jsonServiceClientMock.list(anyString(), anyMap(), eq(CheckoutResponse.class)))
                .thenReturn(mockedResponseList);

        // --- ACT ---
        // 4. Llamamos al método que queremos probar.
        List<CheckoutResponse> result = this.checkoutsOperations.getCheckoutsByMerchant(merchantIdToSearch, searchParams);

        // --- ASSERT ---
        // 5. Verificamos que el resultado es el que esperamos.
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("cko_test_123");
    }
}
