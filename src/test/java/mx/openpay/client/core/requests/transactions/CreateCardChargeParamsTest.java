package mx.openpay.client.core.requests.transactions;

import mx.openpay.client.Card;
import mx.openpay.client.Taxes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class CreateCardChargeParamsTest {

    /**
     * Test case for assigning a valid card to the CreateCardChargeParams instance.
     */
    @Test
    void shouldAssignCardToParams() {
        // Given
        Card testCard = new Card()
                .cardNumber("4111111111111111")
                .holderName("John Doe")
                .cvv2("123")
                .expirationMonth(12)
                .expirationYear(25);

        CreateCardChargeParams params = new CreateCardChargeParams();

        // When
        params.card(testCard);

        // Then
        assertNotNull(params.asMap().get("card"));
        Card assignedCard = (Card) params.asMap().get("card");
        assertEquals("4111111111111111", assignedCard.getCardNumber());
        assertEquals("John Doe", assignedCard.getHolderName());
    }

    /**
     * Test case for assigning a null card to the CreateCardChargeParams instance.
     */
    @Test
    void shouldAssignNullCardToParams() {
        // Given
        CreateCardChargeParams params = new CreateCardChargeParams();

        // When
        params.card(null);

        // Then
        assertEquals(null, params.asMap().get("card"));
    }

    /**
     * Test case to ensure adding a card does not modify other parameters.
     */
    @Test
    void shouldNotAffectOtherParametersWhenAssigningCard() {
        // Given
        CreateCardChargeParams params = new CreateCardChargeParams()
                .description("Test Description")
                .amount(new java.math.BigDecimal("100.00"));

        Card testCard = new Card()
                .cardNumber("4111111111111111")
                .holderName("John Doe");

        // When
        params.card(testCard);

        // Then
        assertEquals("Test Description", params.asMap().get("description"));
        assertEquals(new java.math.BigDecimal("100.00"), params.asMap().get("amount"));
        assertEquals(testCard, params.asMap().get("card"));
    }

    @Test
    void shouldAssignTaxesToParams() {
        // Given
        CreateCardChargeParams params = new CreateCardChargeParams();
        Taxes taxes = new Taxes()
                .baseAmount(new BigDecimal("100.00"))
                .ivaAmount(new BigDecimal("16.00"))
                .consumptionTaxAmount(new BigDecimal("3.50"))
                .airportTax(new BigDecimal("25.75"));

        // When
        CreateCardChargeParams result = params.taxes(taxes);

        // Then
        assertSame(params, result);
        assertNotNull(params.asMap().get("taxes"));
        assertSame(taxes, params.asMap().get("taxes"));
    }

    @Test
    void shouldAssignNullTaxesToParams() {
        // Given
        CreateCardChargeParams params = new CreateCardChargeParams();

        // When
        CreateCardChargeParams result = params.taxes(null);

        // Then
        assertSame(params, result);
        assertEquals(null, params.asMap().get("taxes"));
    }
}