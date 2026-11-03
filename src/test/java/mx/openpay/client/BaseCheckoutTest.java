package mx.openpay.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

class BaseCheckoutTest {

    private final Gson gson = new Gson();

    @Test
    void shouldSetAndReturnAllValues() {
        Taxes taxes = new Taxes()
                .baseAmount(new BigDecimal("100.00"))
                .ivaAmount(new BigDecimal("16.00"))
                .consumptionTaxAmount(new BigDecimal("3.50"))
                .airportTax(new BigDecimal("25.75"));

        Customer customer = new Customer()
                .name("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .externalId("customer-123")
                .requiresAccount(Boolean.FALSE);

        Transaction transaction = new Transaction();
        transaction.setId("txn-123");
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setStatus("completed");

        ConcreteCheckout checkout = new ConcreteCheckout();
        checkout.setId("chk-123");
        checkout.setAmount(new BigDecimal("150.00"));
        checkout.setDescription("Checkout description");
        checkout.setOrderId("order-123");
        checkout.setCurrency("MXN");
        checkout.setIva("16.00");
        checkout.setTaxes(taxes);
        checkout.setTip(Boolean.TRUE);
        checkout.setStatus("completed");
        checkout.setCheckoutLink("https://example.com/checkout/chk-123");
        checkout.setPlanId(42);
        checkout.setCustomer(customer);
        checkout.setTransaction(transaction);

        assertEquals("chk-123", checkout.getId());
        assertEquals(new BigDecimal("150.00"), checkout.getAmount());
        assertEquals("Checkout description", checkout.getDescription());
        assertEquals("order-123", checkout.getOrderId());
        assertEquals("MXN", checkout.getCurrency());
        assertEquals("16.00", checkout.getIva());
        assertEquals(taxes, checkout.getTaxes());
        assertEquals(Boolean.TRUE, checkout.getTip());
        assertEquals("completed", checkout.getStatus());
        assertEquals("https://example.com/checkout/chk-123", checkout.getCheckoutLink());
        assertEquals(Integer.valueOf(42), checkout.getPlanId());
        assertEquals(customer, checkout.getCustomer());
        assertEquals(transaction, checkout.getTransaction());
    }

    @Test
    void shouldSerializeUsingExpectedJsonFieldNames() {
        ConcreteCheckout checkout = new ConcreteCheckout();
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

        JsonObject json = gson.toJsonTree(checkout).getAsJsonObject();

        assertTrue(json.has("order_id"));
        assertTrue(json.has("checkout_link"));
        assertTrue(json.has("plan_id"));
        assertEquals("order-123", json.get("order_id").getAsString());
        assertEquals("https://example.com/checkout/chk-123", json.get("checkout_link").getAsString());
        assertEquals(42, json.get("plan_id").getAsInt());
        assertEquals("chk-123", json.get("id").getAsString());
        assertEquals("150.00", json.get("amount").getAsString());
    }

    @Test
    void shouldDeserializeJsonWithExpectedFieldNames() {
        String json = "{"
                + "\"id\":\"chk-123\","
                + "\"amount\":150.00,"
                + "\"description\":\"Checkout description\","
                + "\"order_id\":\"order-123\","
                + "\"currency\":\"MXN\","
                + "\"iva\":\"16.00\","
                + "\"tip\":true,"
                + "\"status\":\"completed\","
                + "\"checkout_link\":\"https://example.com/checkout/chk-123\","
                + "\"plan_id\":42"
                + "}";

        ConcreteCheckout checkout = gson.fromJson(json, ConcreteCheckout.class);

        assertNotNull(checkout);
        assertEquals("chk-123", checkout.getId());
        assertEquals(new BigDecimal("150.00"), checkout.getAmount());
        assertEquals("order-123", checkout.getOrderId());
        assertEquals("https://example.com/checkout/chk-123", checkout.getCheckoutLink());
        assertEquals(Integer.valueOf(42), checkout.getPlanId());
    }

    @Test
    void shouldIncludeBaseFieldsInToString() {
        ConcreteCheckout checkout = new ConcreteCheckout();
        checkout.setId("chk-123");
        checkout.setAmount(new BigDecimal("150.00"));
        checkout.setDescription("Checkout description");
        checkout.setOrderId("order-123");

        String text = checkout.toString();

        assertTrue(text.contains("chk-123"));
        assertTrue(text.contains("150.00"));
        assertTrue(text.contains("Checkout description"));
        assertTrue(text.contains("order-123"));
    }

    private static class ConcreteCheckout extends BaseCheckout {
    }
}
