package mx.openpay.core.client.full.groups;

import mx.openpay.client.Charge;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.groups.GroupChargeOperations;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupCustomerCardChargesUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private GroupChargeOperations groupChargeOperations;

    private final String FAKE_MERCHANT_ID = "merch_test_id";
    private final String FAKE_CUSTOMER_ID = "cus_test_123";
    private final String FAKE_CHARGE_ID = "charge_test_456";

    /**
     * Conversión de la primera parte de: testCreate_Customer_WithId
     * Lógica: Prueba únicamente la creación de un cargo.
     */
    @Test
    void testCreateCharge() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Preparamos los parámetros para crear el cargo.
        BigDecimal amount = new BigDecimal("10.00");
        CreateCardChargeParams chargeParams = new CreateCardChargeParams()
                .cardId("card_test_id")
                .amount(amount)
                .description("Pago de taxi");

        // 2. Preparamos el objeto Charge que simulará la respuesta de la API.
        Charge chargeResponse = new Charge();
        chargeResponse.setId(FAKE_CHARGE_ID);
        chargeResponse.setAmount(amount);
        chargeResponse.setDescription("Pago de taxi");
        chargeResponse.setStatus("completed");

        // Ajustamos el mock para que espere CUALQUIER MAPA, que es como el SDK
        // envía realmente los parámetros del cargo.
        when(jsonServiceClientMock.post(
                anyString(),
                any(Map.class), // <-- Coincide con el mapa {"amount"=10.00, ...}
                eq(Charge.class)))
                .thenReturn(chargeResponse);

        // --- ACT ---
        Charge result = groupChargeOperations.create(FAKE_MERCHANT_ID, FAKE_CUSTOMER_ID, chargeParams);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(FAKE_CHARGE_ID, result.getId());
        assertEquals(amount, result.getAmount());
        assertEquals("completed", result.getStatus());
    }

    /**
     * Conversión de la parte de reembolso de: testCreate_Customer_WithId
     * Lógica: Prueba únicamente el reembolso de un cargo.
     */
    @Test
    void testRefundCharge() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // La respuesta que esperamos no cambia.
        Charge refundedChargeResponse = new Charge();
        refundedChargeResponse.setId(FAKE_CHARGE_ID);
        refundedChargeResponse.setStatus("refunded");

        // Los parámetros de la petición no cambian.
        RefundParams refundRequestParams = new RefundParams();
        refundRequestParams.chargeId(FAKE_CHARGE_ID);

        // AQUÍ ESTÁ EL CAMBIO:
        // Ajustamos el mock para que espere CUALQUIER MAPA, lo que incluye
        // el mapa vacío {} que vimos en el log.
        when(jsonServiceClientMock.post(
                contains("/refund"),
                any(Map.class), // <-- Coincide con el cuerpo vacío {}
                eq(Charge.class)))
                .thenReturn(refundedChargeResponse);

        // --- ACT ---
        // La llamada al método no necesita cambiar.
        Charge result = groupChargeOperations.refund(FAKE_MERCHANT_ID, FAKE_CUSTOMER_ID, refundRequestParams);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals("refunded", result.getStatus());
    }
}
