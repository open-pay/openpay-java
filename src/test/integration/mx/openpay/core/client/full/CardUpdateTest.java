package mx.openpay.core.client.full;

import lombok.extern.slf4j.Slf4j;
import mx.openpay.client.Address;
import mx.openpay.client.Card;
import mx.openpay.client.CardUpdateRequest;
import mx.openpay.client.Customer;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests to Update Cards
 *
 * @author jemima.del.angel
 * @since 08/05/2020
 */
@Slf4j
public class CardUpdateTest extends BaseTest {


    /**
     * Update Merchant Card
     * URL: (PUT)   /v1/{merchantId}/cards/{cardId}
     *
     * @throws OpenpayServiceException     Openpay Service Exception
     * @throws ServiceUnavailableException Service Unavailable Exception
     */
    @Test
    public void testCreateMerchantCardToUpdate() throws OpenpayServiceException, ServiceUnavailableException {
        log.info("--- Update Merchant Card ---");
        Card card = this.api.cards().create(this.createCard("5105105105105100"));
        log.info("Customer credit card: {}", card.getId());
        log.info("Merchant credit card <<Holder's name>> before update: {}", card.getHolderName());
        this.api.cards().update(card.getId(), getUpdatableCardJson(card));
        card = this.api.cards().get(card.getId());
        log.info("Merchant credit card <<Holder's name>> after update: {}", card.getHolderName());
        this.api.cards().delete(card.getId());
        log.info("-------------------");
    }

    /**
     * Update Customer Card
     * URL: (PUT)   /v1/{merchantId}/customers/{customerId}/cards/{cardId}
     *
     * @throws OpenpayServiceException     Openpay Service Exception
     * @throws ServiceUnavailableException Service Unavailable Exception
     */
    @Test
    public void testCreateCustomerCardToUpdate() throws OpenpayServiceException, ServiceUnavailableException {
        log.info("--- Update Customer Card ---");
        Customer customer = this.api.customers().create(
                new Customer().name("Jemima").lastName("Del Angel").email("jemda@example.com").phoneNumber("525541703567")
                        .address(getAddress()));
        Card customerCard = this.api.cards().create(customer.getId(), createCard("5555555555554444"));
        log.info("Customer credit card: {}", customerCard.getId());
        log.info("Merchant credit card <<Holder's name>> before update: {}", customerCard.getHolderName());
        this.api.cards().update(customer.getId(), customerCard.getId(), getUpdatableCardJson(customerCard));
        customerCard = this.api.cards().get(customer.getId(), customerCard.getId());
        log.info("Merchant credit card <<Holder's name>> after update: {}", customerCard.getHolderName());
        this.api.cards().delete(customer.getId(), customerCard.getId());
        this.api.customers().delete(customer.getId());
        log.info("-------------------");
    }

    /**
     * Json to Update Card
     *
     * @param card Original Card
     * @return Updatable Card Request
     */
    private CardUpdateRequest getUpdatableCardJson(final Card card) {
        return new CardUpdateRequest().cvv2(card.getCvv2())
                .holderName(card.getHolderName() + " Updated");
    }

    /**
     * Create Card
     *
     * @param number Card Number
     * @return The Card
     * @throws OpenpayServiceException     Openpay Service Exception
     * @throws ServiceUnavailableException Service Unavailable Exception
     */
    private Card createCard(final String number) throws OpenpayServiceException, ServiceUnavailableException {
        return new Card().cardNumber(number) // No dashes or spaces
                .holderName("Jemima Del Ángel San Martín")
                .cvv2(number.startsWith("3") ? "1234" : "422")
                .expirationMonth(12).expirationYear(30);
    }

    /**
     * Create Address
     *
     * @return The Address
     */
    private static final Address getAddress() {
        return new Address().line1("Nueva Cecilia #08").city("Tampico").postalCode("12345")
                .state("Tamaulipas").countryCode("MX");
    }
}
