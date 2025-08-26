package mx.openpay.core.client.full.groups;

import mx.openpay.client.Card;
import mx.openpay.client.core.JsonServiceClient;
import mx.openpay.client.core.operations.groups.GroupCardOperations;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupCustomerCardUnitTest {

    @Mock
    private JsonServiceClient jsonServiceClientMock;

    @InjectMocks
    private GroupCardOperations groupCardOperations;

    private final String FAKE_CUSTOMER_ID = "cus_test_12345";
    private final String FAKE_CARD_ID = "card_test_67890";

    /**
     * Conversión de: testCreateCustomerCard
     * Lógica: Simulamos la respuesta de la API al crear una tarjeta y verificamos que
     * nuestro servicio parsea y devuelve el objeto Card correctamente.
     */
    @Test
    void testCreateCard() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        Card cardToCreate = new Card().holderName("Juanito Perez Nunez");

        Card cardResponse = new Card();
        cardResponse.setId(FAKE_CARD_ID);
        cardResponse.setHolderName("Juanito Perez Nunez");
        cardResponse.setCardNumber("42424242XXXX4242");

        when(jsonServiceClientMock.post(anyString(), any(Card.class), eq(Card.class)))
                .thenReturn(cardResponse);

        // --- ACT ---
        Card result = groupCardOperations.create(FAKE_CUSTOMER_ID, cardToCreate);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(FAKE_CARD_ID, result.getId());
        assertEquals("42424242XXXX4242", result.getCardNumber());
    }

    /**
     * Conversión de: testGetCustomerCard
     * Lógica: Simulamos la respuesta de la API al obtener una tarjeta específica.
     */
    @Test
    void testGetCard() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        Card cardResponse = new Card();
        cardResponse.setId(FAKE_CARD_ID);
        cardResponse.setHolderName("Juanito Perez Nunez");

        when(jsonServiceClientMock.get(anyString(), eq(Card.class))).thenReturn(cardResponse);

        // --- ACT ---
        Card result = groupCardOperations.get(FAKE_CUSTOMER_ID, FAKE_CARD_ID);

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(FAKE_CARD_ID, result.getId());
    }

    /**
     * Conversión de: testDeleteCustomerCard
     * Lógica: Verificamos que al llamar a delete() en nuestro servicio, se invoca
     * el método delete() del cliente HTTP con la URL correcta.
     */
    @Test
    void testDeleteCard() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        doNothing().when(jsonServiceClientMock).delete(anyString());

        // --- ACT ---
        groupCardOperations.delete(FAKE_CUSTOMER_ID, FAKE_CARD_ID);

        // --- ASSERT ---
        verify(jsonServiceClientMock, times(1)).delete(contains(FAKE_CARD_ID));
    }

    /**
     * Conversión de: test...DoesNotExist (get y delete)
     * Lógica: Unificamos los tests de error. Simulamos que la API lanza una excepción
     * y verificamos que nuestro servicio la maneja como se espera.
     */
    @Test
    void testOperationOnNonExistentCard_ShouldThrowException() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        OpenpayServiceException exceptionToThrow = new OpenpayServiceException("Resource not found");
        exceptionToThrow.setHttpCode(404);

        when(jsonServiceClientMock.get(anyString(), eq(Card.class))).thenThrow(exceptionToThrow);
        doThrow(exceptionToThrow).when(jsonServiceClientMock).delete(anyString());

        // --- ACT & ASSERT ---
        // Probar GET
        assertThrows(OpenpayServiceException.class, () -> {
            groupCardOperations.get(FAKE_CUSTOMER_ID, "card_does_not_exist");
        });

        // Probar DELETE
        assertThrows(OpenpayServiceException.class, () -> {
            groupCardOperations.delete(FAKE_CUSTOMER_ID, "card_does_not_exist");
        });
    }

    /**
     * Conversión de: testListCustomerCards
     * Lógica: Simulamos una respuesta de la API con una lista de tarjetas.
     */
    @Test
    void testListCards() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        Card card1 = new Card();
        card1.setId("card_aaa");
        Card card2 = new Card();
        card2.setId("card_bbb");
        List<Card> cardListResponse = Arrays.asList(card1, card2);

        when(jsonServiceClientMock.list(anyString(), anyMap(), any(Class.class)))
                .thenReturn(cardListResponse);

        // --- ACT ---
        List<Card> result = groupCardOperations.list(FAKE_CUSTOMER_ID, SearchParams.search());

        // --- ASSERT ---
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("card_aaa", result.get(0).getId());
    }

    /**
     * Conversión de: testListCustomerCards_Empty
     * Lógica: Simulamos una respuesta de la API con una lista vacía.
     */
    @Test
    void testListCards_Empty() throws OpenpayServiceException, ServiceUnavailableException {
        // --- ARRANGE ---
        // Usamos nullable(Map.class) para indicarle al mock que el segundo
        // argumento puede ser un Map O puede ser null.
        when(jsonServiceClientMock.list(
                anyString(),
                nullable(Map.class), // <-- EL CAMBIO ESTÁ AQUÍ
                any(Class.class)))
                .thenReturn(Collections.emptyList());

        // --- ACT ---
        // Esta llamada con 'null' ahora coincidirá con la preparación del mock.
        List<Card> result = groupCardOperations.list(FAKE_CUSTOMER_ID, null);

        // --- ASSERT ---
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
