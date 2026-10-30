package mx.openpay.client;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckoutTest {

    private Gson gson;

    @Test
    void shouldSetAndReturnBaseAndSpecificValues() {
        Checkout checkout = new Checkout();
        checkout.setId("chk-123");
        checkout.setAmount(new BigDecimal("150.00"));
        checkout.setDescription("Checkout description");
        checkout.setOrderId("order-123");
        checkout.setCurrency("MXN");
        checkout.setIva("16.00");
        checkout.setTip(Boolean.TRUE);
        checkout.setStatus("completed");
        checkout.setCheckoutLink("https://example.com/checkout/chk-123");
        checkout.setPlanId(42);
        checkout.setExpirationDate(new Date(1_000L));
        checkout.setOriginChannel("web");

        assertEquals("chk-123", checkout.getId());
        assertEquals(new BigDecimal("150.00"), checkout.getAmount());
        assertEquals("Checkout description", checkout.getDescription());
        assertEquals("order-123", checkout.getOrderId());
        assertEquals("MXN", checkout.getCurrency());
        assertEquals("16.00", checkout.getIva());
        assertEquals(Boolean.TRUE, checkout.getTip());
        assertEquals("completed", checkout.getStatus());
        assertEquals("https://example.com/checkout/chk-123", checkout.getCheckoutLink());
        assertEquals(Integer.valueOf(42), checkout.getPlanId());
        assertEquals(new Date(1_000L), checkout.getExpirationDate());
        assertEquals("web", checkout.getOriginChannel());
    }

    @Test
    void shouldSerializeExpectedFieldNames() {
        Checkout checkout = new Checkout();
        checkout.setExpirationDate(new Date(1_000L));
        checkout.setOriginChannel("web");
        checkout.setOrderId("order-123");
        checkout.setCheckoutLink("https://example.com/checkout/chk-123");
        checkout.setPlanId(42);

        JsonObject json = gson.toJsonTree(checkout).getAsJsonObject();

        assertTrue(json.has("expiration_date"));
        assertTrue(json.has("origin"));
        assertTrue(json.has("order_id"));
        assertTrue(json.has("checkout_link"));
        assertTrue(json.has("plan_id"));
        assertEquals("web", json.get("origin").getAsString());
        assertEquals("order-123", json.get("order_id").getAsString());
        assertEquals("https://example.com/checkout/chk-123", json.get("checkout_link").getAsString());
        assertEquals(42, json.get("plan_id").getAsInt());
    }

    @Test
    void shouldIncludeBaseFieldsInToString() {
        Checkout checkout = new Checkout();
        checkout.setId("chk-123");
        checkout.setAmount(new BigDecimal("150.00"));
        checkout.setDescription("Checkout description");
        checkout.setOrderId("order-123");
        checkout.setOriginChannel("web");

        String text = checkout.toString();

        assertTrue(text.contains("chk-123"));
        assertTrue(text.contains("150.00"));
        assertTrue(text.contains("Checkout description"));
        assertTrue(text.contains("order-123"));
        assertTrue(text.contains("web"));
    }


    private Checkout checkout;
    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();

        // Definimos un formato estándar para evitar problemas de Locale en las pruebas de Gson
        String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        dateFormat = new SimpleDateFormat(pattern);
        gson = new GsonBuilder().setDateFormat(pattern).create();
    }

    @Test
    @DisplayName("Debe asignar y obtener correctamente los valores de ExpirationDate y OriginChannel")
    void testGettersAndSetters() throws ParseException {
        // Arrange
        Date expectedDate = dateFormat.parse("2026-12-31T23:59:59Z");
        String expectedOrigin = "MOBILE_APP";

        // Act
        checkout.setExpirationDate(expectedDate);
        checkout.setOriginChannel(expectedOrigin);

        // Assert
        assertEquals(expectedDate, checkout.getExpirationDate(), "El expirationDate no coincide");
        assertEquals(expectedOrigin, checkout.getOriginChannel(), "El originChannel no coincide");
    }

    @Test
    @DisplayName("Debe generar el toString correctamente incluyendo los campos de la clase")
    void testToString() throws ParseException {
        // Arrange
        Date testDate = dateFormat.parse("2026-12-31T23:59:59Z");
        checkout.setExpirationDate(testDate);
        checkout.setOriginChannel("MOBILE_APP");

        // Act
        String result = checkout.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Checkout"), "El toString debe contener el nombre de la clase");
        assertTrue(result.contains("originChannel=MOBILE_APP"), "El toString debe contener el valor de originChannel");
        assertTrue(result.contains("expirationDate="), "El toString debe contener la referencia de expirationDate");
    }

    @Test
    @DisplayName("Debe serializar el objeto a JSON respetando la anotación @SerializedName")
    void testGsonSerialization() throws ParseException {
        // Arrange
        Date testDate = dateFormat.parse("2026-12-31T23:59:59Z");
        checkout.setExpirationDate(testDate);
        checkout.setOriginChannel("WEB");

        // Act
        String jsonResult = gson.toJson(checkout);

        // Assert
        assertTrue(jsonResult.contains("\"expiration_date\":\"2026-12-31T23:59:59Z\""), "El JSON debe usar 'expiration_date' y el formato correcto");
        assertTrue(jsonResult.contains("\"origin\":\"WEB\""), "El JSON debe usar el nombre 'origin' para originChannel");
    }

    @Test
    @DisplayName("Debe deserializar un JSON al objeto respetando la anotación @SerializedName")
    void testGsonDeserialization() throws ParseException {
        // Arrange
        String jsonInput = "{\"expiration_date\":\"2026-12-31T23:59:59Z\", \"origin\":\"WEB\"}";
        Date expectedDate = dateFormat.parse("2026-12-31T23:59:59Z");

        // Act
        Checkout parsedCheckout = gson.fromJson(jsonInput, Checkout.class);

        // Assert
        assertNotNull(parsedCheckout);
        assertEquals("WEB", parsedCheckout.getOriginChannel(), "El canal de origen no se deserializó correctamente");
        assertEquals(expectedDate, parsedCheckout.getExpirationDate(), "La fecha de expiración no se deserializó correctamente");
    }
}
