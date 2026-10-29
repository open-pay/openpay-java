package mx.openpay.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

class TaxesTest {

    private final Gson gson = new Gson();

    @Test
    void shouldSetAndReturnAllValuesWithFluentMethods() {
        BigDecimal baseAmount = new BigDecimal("100.00");
        BigDecimal ivaAmount = new BigDecimal("16.00");
        BigDecimal consumptionTaxAmount = new BigDecimal("3.50");
        BigDecimal airportTax = new BigDecimal("25.75");

        Taxes taxes = new Taxes();
        Taxes result = taxes.baseAmount(baseAmount)
                .ivaAmount(ivaAmount)
                .consumptionTaxAmount(consumptionTaxAmount)
                .airportTax(airportTax);

        assertSame(taxes, result);
        assertEquals(baseAmount, taxes.getBaseAmount());
        assertEquals(ivaAmount, taxes.getIvaAmount());
        assertEquals(consumptionTaxAmount, taxes.getConsumptionTaxAmount());
        assertEquals(airportTax, taxes.getAirportTax());
    }

    @Test
    void shouldSerializeWithExpectedJsonFieldNames() {
        Taxes taxes = new Taxes()
                .baseAmount(new BigDecimal("100.00"))
                .ivaAmount(new BigDecimal("16.00"))
                .consumptionTaxAmount(new BigDecimal("3.50"))
                .airportTax(new BigDecimal("25.75"));

        JsonObject json = gson.toJsonTree(taxes).getAsJsonObject();

        assertTrue(json.has("base_amount"));
        assertTrue(json.has("iva_amount"));
        assertTrue(json.has("consumption_tax_amount"));
        assertTrue(json.has("airport_tax"));
        assertEquals("100.00", json.get("base_amount").getAsString());
        assertEquals("16.00", json.get("iva_amount").getAsString());
        assertEquals("3.50", json.get("consumption_tax_amount").getAsString());
        assertEquals("25.75", json.get("airport_tax").getAsString());
    }
}
