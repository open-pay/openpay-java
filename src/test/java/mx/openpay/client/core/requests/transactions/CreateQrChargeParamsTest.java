package mx.openpay.client.core.requests.transactions;

import mx.openpay.client.Customer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateQrChargeParamsTest {

    /**
     * Test case for assigning the method to the CreateQrChargeParams instance.
     */
    @Test
    void shouldAssignMethodToParams() {
        // When
        CreateQrChargeParams params = new CreateQrChargeParams();

        // Then
        assertEquals("qr", params.asMap().get("method"));
    }

    /**
     * Test case for assigning the iva to the CreateQrChargeParams instance.
     */
    @Test
    void shouldAssignIvaToParams() {
        // Given
        String iva = "19.00";
        // When
        CreateQrChargeParams params = new CreateQrChargeParams().iva(iva);

        // Then
        assertEquals(iva, params.asMap().get("iva"));
    }

    // ... existing code ...

    /**
     * Test case for assigning null values to the CreateQrChargeParams instance.
     */
    @Test
    void shouldAssignNullValuesToParams() {
        // Given
        CreateQrChargeParams params = new CreateQrChargeParams();

        // When
        params.amount(null)
                .iva(null)
                .currency(null)
                .description(null)
                .orderId(null);

        // Then
        assertNull(params.asMap().get("amount"));
        assertNull(params.asMap().get("iva"));
        assertNull(params.asMap().get("currency"));
        assertNull(params.asMap().get("description"));
        assertNull(params.asMap().get("order_id"));
        assertTrue(params.asMap().containsKey("method"));
        assertTrue(params.asMap().containsKey("order_id"));
    }

    /**
     * Test case to ensure every parameter is kept when chaining the builder methods.
     */
    @Test
    void shouldKeepAllParametersWhenChaining() {
        // Given
        BigDecimal amount = new BigDecimal("250.50");

        // When
        CreateQrChargeParams params = new CreateQrChargeParams()
                .amount(amount)
                .currency("MXN")
                .description("QR charge")
                .orderId("oid-00052");

        // Then
        assertEquals(5, params.asMap().size());
        assertEquals("qr", params.asMap().get("method"));
        assertEquals(amount, params.asMap().get("amount"));
        assertEquals("MXN", params.asMap().get("currency"));
        assertEquals("QR charge", params.asMap().get("description"));
        assertEquals("oid-00052", params.asMap().get("order_id"));
    }

    // ... existing code ...

    /**
     * Test case to ensure all parameters including new ones are retained when chaining.
     */
    @Test
    void shouldKeepAllParametersIncludingNewOnesWhenChaining() {
        // Given
        BigDecimal amount = new BigDecimal("500.00");
        String iva = "80.00";
        Date dueDate = new Date();
        Customer customer = new Customer().name("Test").email("test@example.com");

        // When
        CreateQrChargeParams params = new CreateQrChargeParams()
                .amount(amount)
                .iva(iva)
                .currency("MXN")
                .description("QR test")
                .orderId("oid-99")
                .originChannel("MOBILE")
                .productType("PREMIUM")
                .customer(customer)
                .dueDate(dueDate);

        // Then
        assertEquals(10, params.asMap().size());
        assertEquals("qr", params.asMap().get("method"));
        assertEquals(amount, params.asMap().get("amount"));
        assertEquals(iva, params.asMap().get("iva"));
        assertEquals("MXN", params.asMap().get("currency"));
        assertEquals("QR test", params.asMap().get("description"));
        assertEquals("oid-99", params.asMap().get("order_id"));
        assertEquals("MOBILE", params.asMap().get("origin_channel"));
        assertEquals("PREMIUM", params.asMap().get("product_type"));
        assertSame(customer, params.asMap().get("customer"));
        assertSame(dueDate, params.asMap().get("due_date"));
    }
}