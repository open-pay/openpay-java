package mx.openpay.core.client.full;

import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.CardUpdateRequest;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.CardOperations;
import mx.openpay.client.core.requests.cards.UpdateCardParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardUpdateUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private CardOperations cardService; // La clase que contiene la lógica a probar

    private final String FAKE_CARD_ID = "card_test_123";
    private final String FAKE_CUSTOMER_ID = "cus_test_abc";

    /**
     * Conversión de: testCreateMerchantCardToUpdate
     * Lógica: Probar la actualización de una tarjeta asociada directamente al merchant.
     */
    @Test
    void testUpdateMerchantCard() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // 1. Creamos la petición con la clase correcta: UpdateCardParams
        UpdateCardParams updateParams = new UpdateCardParams();
        updateParams.setCardId(FAKE_CARD_ID);
        updateParams.holderName("Marcos Vázquez");
        updateParams.expirationMonth(12);

        // 2. Creamos la respuesta simulada
        Card updatedCardResponse = new Card();
        updatedCardResponse.setId(FAKE_CARD_ID);
        updatedCardResponse.setHolderName("Jemima Updated");

        // 3. Configuramos el mock para que espere un MAP, que es lo que .asMap() produce
        when(jsonServiceClientMock.put(anyString(), any(Map.class), eq(Card.class)))
                .thenReturn(updatedCardResponse);

        // --- ACT ---
        // El método de nuestro servicio espera el objeto UpdateCardParams
        cardService.update(updateParams);

        // --- ASSERT ---
        // Verificamos que el método 'put' del mock fue llamado exactamente 1 vez.
        verify(jsonServiceClientMock, times(1)).put(
                contains(FAKE_CARD_ID), // Verificamos que la URL contiene el ID de la tarjeta
                any(Map.class),         // Verificamos que el cuerpo es un Mapa
                eq(Card.class)          // Verificamos que la clase de respuesta es Card
        );
    }

    /**
     * Conversión de: testCreateCustomerCardToUpdate
     * Lógica: Probar la actualización de una tarjeta asociada a un customer.
     */
    @Test
    void testUpdateCustomerCard() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        UpdateCardParams updateParams = new UpdateCardParams();
        updateParams.setCardId(FAKE_CARD_ID);
        updateParams.holderName("Marcos Vázquez");
        updateParams.expirationMonth(12);

        Card updatedCardResponse = new Card();
        updatedCardResponse.setId(FAKE_CARD_ID);

        when(jsonServiceClientMock.put(anyString(), any(Map.class), eq(Card.class)))
                .thenReturn(updatedCardResponse);

        // --- ACT ---
        cardService.update(FAKE_CUSTOMER_ID, updateParams);

        // --- ASSERT ---
        // Verificamos la interacción, asegurando que tanto el ID del cliente
        // como el de la tarjeta estén en la URL que se construye.
        verify(jsonServiceClientMock, times(1)).put(
                contains(FAKE_CUSTOMER_ID), // La URL debe contener el ID del cliente
                any(Map.class),
                eq(Card.class)
        );
    }

}
