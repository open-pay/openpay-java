package mx.openpay.client;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaxesIntegrationTest {

    @Test
    void testSetBaseAmountWithValidValue() {
        // Arrange
        Taxes taxes = new Taxes();
        BigDecimal expectedBaseAmount = new BigDecimal("100.00");

        // Act
        Taxes result = taxes.baseAmount(expectedBaseAmount);

        // Assert
        assertNotNull(result);
        assertEquals(expectedBaseAmount, taxes.getBaseAmount());
    }

    @Test
    void testSetBaseAmountWithNullValue() {
        // Arrange
        Taxes taxes = new Taxes();

        // Act
        Taxes result = taxes.baseAmount(null);

        // Assert
        assertNotNull(result);
        assertEquals(null, taxes.getBaseAmount());
    }

    @Test
    void testSetBaseAmountWithLargeDecimalValue() {
        // Arrange
        Taxes taxes = new Taxes();
        BigDecimal expectedBaseAmount = new BigDecimal("9876543210.987654321");

        // Act
        Taxes result = taxes.baseAmount(expectedBaseAmount);

        // Assert
        assertNotNull(result);
        assertEquals(expectedBaseAmount, taxes.getBaseAmount());
    }
}
