package mx.openpay.core.client.full;

import mx.openpay.client.Charge;
import mx.openpay.client.PaymentMethod;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.ChargeOperations;
import mx.openpay.client.core.requests.transactions.CreateAlipayChargeParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerAlipayChargeUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private ChargeOperations chargeService; // La clase que contiene la lógica a probar

    private final String FAKE_CUSTOMER_ID = "cus_test_123";
    private final String FAKE_CHARGE_ID = "charge_test_456";

    /**
     * Conversión de: testCreateAlipayCharge
     * Lógica: Probar la creación de un cargo con Alipay.
     */
    @Test
    void testCreateAlipayCharge() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Preparamos los parámetros de entrada para la creación del cargo.
        BigDecimal amount = new BigDecimal("10.00");
        CreateAlipayChargeParams alipayParams = new CreateAlipayChargeParams()
                .amount(amount)
                .description("Pago de taxi")
                .redirectUrl("https://www.example.com/alipayRedirection");

        // 2. Creamos el objeto 'Charge' que simulará la respuesta de la API.
        Charge chargeResponse = new Charge();
        chargeResponse.setId(FAKE_CHARGE_ID);
        chargeResponse.setAmount(amount);
        chargeResponse.setDescription("Pago de taxi");
        chargeResponse.setStatus("charge_pending");
        chargeResponse.setDueDate(new Date());
        // Simulamos también el objeto anidado PaymentMethod
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType("alipay");
        paymentMethod.setUrl("https://pay.openpay.mx/alipay/fake_url");
        chargeResponse.setPaymentMethod(paymentMethod);

        // 3. Configuramos el mock para la llamada POST. Asumimos que convierte los params a un Map.
        when(jsonServiceClientMock.post(anyString(), any(Map.class), eq(Charge.class)))
                .thenReturn(chargeResponse);

        // --- ACT ---
        // 4. Llamamos al método que queremos probar.
        Charge result = chargeService.createCharge(FAKE_CUSTOMER_ID, alipayParams);

        // --- ASSERT ---
        // 5. Verificamos que el resultado es el objeto que nuestro mock devolvió.
        assertNotNull(result);
        assertNotNull(result.getPaymentMethod());
        assertNotNull(result.getPaymentMethod().getUrl());
        assertEquals("charge_pending", result.getStatus());
        assertThat(amount).isEqualByComparingTo(result.getAmount());
    }

    /**
     * Conversión de: testGetAlipayCharge
     * Lógica: Probar la obtención de un cargo existente.
     */
    @Test
    void testGetAlipayCharge() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Preparamos la respuesta que simulará el cargo existente.
        BigDecimal amount = new BigDecimal("10.00");
        Charge chargeResponse = new Charge();
        chargeResponse.setId(FAKE_CHARGE_ID);
        chargeResponse.setAmount(amount);
        chargeResponse.setDescription("Pago de taxi");
        chargeResponse.setStatus("charge_pending");

        // 2. Configuramos el mock para la llamada GET.
        when(jsonServiceClientMock.get(anyString(), eq(Charge.class))).thenReturn(chargeResponse);

        // --- ACT ---
        // 3. Llamamos al método 'get' que queremos probar.
        Charge result = chargeService.get(FAKE_CUSTOMER_ID, FAKE_CHARGE_ID);

        // --- ASSERT ---
        // 4. Verificamos que el resultado es el esperado.
        assertNotNull(result);
        assertEquals(FAKE_CHARGE_ID, result.getId());
        assertEquals("charge_pending", result.getStatus());
        assertThat(amount).isEqualByComparingTo(result.getAmount());
    }
}
