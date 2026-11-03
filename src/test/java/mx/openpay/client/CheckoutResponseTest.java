package mx.openpay.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckoutResponseTest {

    private final Gson gson =new Gson();

    @Test
    void shouldSetAndReturnBaseAndSpecificValues() {
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setId("chk-123");
        checkoutResponse.setOrderId("order-123");
        checkoutResponse.setCheckoutLink("https://example.com/checkout/chk-123");
        checkoutResponse.setPlanId(42);
        checkoutResponse.setCreationDate("2026-05-26T10:30:00-06:00");
        checkoutResponse.setExpirationDate("2026-06-26T10:30:00-06:00");

        assertEquals("chk-123", checkoutResponse.getId());
        assertEquals("order-123", checkoutResponse.getOrderId());
        assertEquals("https://example.com/checkout/chk-123", checkoutResponse.getCheckoutLink());
        assertEquals(Integer.valueOf(42), checkoutResponse.getPlanId());
        assertEquals("2026-05-26T10:30:00-06:00", checkoutResponse.getCreationDate());
        assertEquals("2026-06-26T10:30:00-06:00", checkoutResponse.getExpirationDate());
    }

    @Test
    void shouldSerializeExpectedFieldNames() {
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setCreationDate("2026-05-26T10:30:00-06:00");
        checkoutResponse.setExpirationDate("2026-06-26T10:30:00-06:00");
        checkoutResponse.setOrderId("order-123");
        checkoutResponse.setCheckoutLink("https://example.com/checkout/chk-123");
        checkoutResponse.setPlanId(42);

        JsonObject json = gson.toJsonTree(checkoutResponse).getAsJsonObject();

        assertTrue(json.has("creation_date"));
        assertTrue(json.has("expiration_date"));
        assertTrue(json.has("order_id"));
        assertTrue(json.has("checkout_link"));
        assertTrue(json.has("plan_id"));
        assertEquals("2026-05-26T10:30:00-06:00", json.get("creation_date").getAsString());
        assertEquals("2026-06-26T10:30:00-06:00", json.get("expiration_date").getAsString());
        assertEquals("order-123", json.get("order_id").getAsString());
        assertEquals("https://example.com/checkout/chk-123", json.get("checkout_link").getAsString());
        assertEquals(42, json.get("plan_id").getAsInt());
    }

    @Test
    void shouldIncludeBaseFieldsInToString() {
        CheckoutResponse checkoutResponse = new CheckoutResponse();
        checkoutResponse.setId("chk-123");
        checkoutResponse.setOrderId("order-123");
        checkoutResponse.setCreationDate("2026-05-26T10:30:00-06:00");

        String text = checkoutResponse.toString();

        assertTrue(text.contains("chk-123"));
        assertTrue(text.contains("order-123"));
        assertTrue(text.contains("2026-05-26T10:30:00-06:00"));
    }
    private CheckoutResponse checkoutResponse;

    @BeforeEach
    void setUp() {
        checkoutResponse = new CheckoutResponse();
    }

    @Test
    @DisplayName("Debe asignar y obtener correctamente los valores de CreationDate y ExpirationDate")
    void testGettersAndSetters() {
        // Arrange
        String expectedCreationDate = "2026-05-26T10:00:00Z";
        String expectedExpirationDate = "2026-05-27T10:00:00Z";

        // Act
        checkoutResponse.setCreationDate(expectedCreationDate);
        checkoutResponse.setExpirationDate(expectedExpirationDate);

        // Assert
        assertEquals(expectedCreationDate, checkoutResponse.getCreationDate(), "El creationDate no coincide");
        assertEquals(expectedExpirationDate, checkoutResponse.getExpirationDate(), "El expirationDate no coincide");
    }

    @Test
    @DisplayName("Debe generar el toString correctamente incluyendo los campos de la clase")
    void testToString() {
        // Arrange
        checkoutResponse.setCreationDate("2026-05-26");
        checkoutResponse.setExpirationDate("2026-05-30");

        // Act
        String result = checkoutResponse.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("CheckoutResponse"), "El toString debe contener el nombre de la clase");
        assertTrue(result.contains("creationDate=2026-05-26"), "El toString debe contener el valor de creationDate");
        assertTrue(result.contains("expirationDate=2026-05-30"), "El toString debe contener el valor de expirationDate");
        // Nota: Como usas callSuper=true, el resultado de toString() también incluirá lo que devuelva BaseCheckout.toString()
    }

    @Test
    @DisplayName("Debe serializar el objeto a JSON respetando la anotación @SerializedName")
    void testGsonSerialization() {
        // Arrange
        checkoutResponse.setCreationDate("2026-05-26");
        checkoutResponse.setExpirationDate("2026-05-30");

        // Act
        String jsonResult = gson.toJson(checkoutResponse);

        // Assert
        assertTrue(jsonResult.contains("\"creation_date\":\"2026-05-26\""), "El JSON debe usar el nombre 'creation_date'");
        assertTrue(jsonResult.contains("\"expiration_date\":\"2026-05-30\""), "El JSON debe usar el nombre 'expiration_date'");
    }

    @Test
    @DisplayName("Debe deserializar un JSON al objeto respetando la anotación @SerializedName")
    void testGsonDeserialization() {
        // Arrange
        String jsonInput = "{\"creation_date\":\"2026-05-26\", \"expiration_date\":\"2026-05-30\"}";

        // Act
        CheckoutResponse parsedResponse = gson.fromJson(jsonInput, CheckoutResponse.class);

        // Assert
        assertNotNull(parsedResponse);
        assertEquals("2026-05-26", parsedResponse.getCreationDate());
        assertEquals("2026-05-30", parsedResponse.getExpirationDate());
    }
}
